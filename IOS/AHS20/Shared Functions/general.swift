//
//  extraFunc.swift
//  AHS20
//
//  Created by Richard Wei on 4/21/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit
import AudioToolbox
import SystemConfiguration
import FirebaseDatabase
import SDWebImage
import Firebase

var resetUpArticles = false;
var unreadNotifCount = 0;

// swift file with shared functions and extensions between files


struct articleData: Codable {
    var articleID: String?;
    var articleTitle: String?;
    var articleUnixEpoch: Int64?; // unix epoch time stamp
    var articleBody: String?;
    var articleAuthor: String?;
    var articleImages: [String]?; // list of image urls
    var articleVideoIDs: [String]?;
    var articleCatagory: String?;
    var isFeatured = false;
    var hasHTML = false;
}

var internetConnected = false;
var homeArticleList = [[articleData]](repeating: [articleData](), count: 3); // size of 4 rows, featured, asb, sports, district
var bulletinArticleList = [[bulletinArticleData]](repeating: [bulletinArticleData](), count: 6); // size of 6 rows, seniors, colleges, events, athletics, reference, and others

struct notificationData: Codable{
    var messageID: String?;
    var notificationTitle: String?;
    var notificationBody: String?;
    var notificationUnixEpoch: Int64?;
    var notificationArticleID: String?; // articleID pointer
    var notificationCatagory: Int?; // 0 - 3 : 0 is sports 1 is asb 2 is district 3 is bulletin
}

class CustomUIButton: UIButton{
    var articleIndex = -1;
    var articleCompleteData = articleData();
}

class notificationUIButton: CustomUIButton{
    var alreadyRead = false;
    var notificationCompleteData = notificationData();
}

struct bulletinArticleData: Codable {
    var articleID: String?;
    var articleTitle: String?;
    var articleUnixEpoch: Int64?; // unix epoch time stamp
    var articleBody: String?;
    var articleCatagory: String?;
    var articleType = -1;
    var hasHTML = false;
}


var ref: DatabaseReference!; // database reference

func setUpConnection(){
    if (Reachability.isConnectedToNetwork()){
        internetConnected = true;
        Database.database().goOnline();
        ref = Database.database().reference();
    }
    else{
        internetConnected = false;
        Database.database().goOffline();
        ref = nil;
    }
}

func getTitleDateAndMonth() -> String {
    let dateObj = Date();
    let calender = Calendar.current;
    let dayInt = calender.component(.day , from: dateObj);
    
    let monthInt = Calendar.current.dateComponents([.month], from: Date()).month;
    let monthStr = Calendar.current.monthSymbols[monthInt!-1];
    return String(monthStr) + " " + String(dayInt);
}


final public class Reachability {

    class func isConnectedToNetwork() -> Bool {

        var zeroAddress = sockaddr_in(sin_len: 0, sin_family: 0, sin_port: 0, sin_addr: in_addr(s_addr: 0), sin_zero: (0, 0, 0, 0, 0, 0, 0, 0))
        zeroAddress.sin_len = UInt8(MemoryLayout.size(ofValue: zeroAddress))
        zeroAddress.sin_family = sa_family_t(AF_INET)

        let defaultRouteReachability = withUnsafePointer(to: &zeroAddress) {
            $0.withMemoryRebound(to: sockaddr.self, capacity: 1) {zeroSockAddress in
                SCNetworkReachabilityCreateWithAddress(nil, zeroSockAddress)
            }
        }

        var flags: SCNetworkReachabilityFlags = SCNetworkReachabilityFlags(rawValue: 0)
        if SCNetworkReachabilityGetFlags(defaultRouteReachability!, &flags) == false {
            return false
        }

        // Working for Cellular and WIFI
        let isReachable = (flags.rawValue & UInt32(kSCNetworkFlagsReachable)) != 0
        let needsConnection = (flags.rawValue & UInt32(kSCNetworkFlagsConnectionRequired)) != 0

        return (isReachable && !needsConnection)

    }
}



final class SegueFromRight: UIStoryboardSegue {
    override func perform() {
        let src = self.source
        let dst = self.destination
        
        src.view.superview?.insertSubview(dst.view, aboveSubview: src.view)
        dst.view.transform = CGAffineTransform(translationX: src.view.frame.size.width, y: 0)

        UIView.animate(withDuration: 0.18,
                              delay: 0.0,
                            options: .curveEaseInOut,
                         animations: {
                                dst.view.transform = CGAffineTransform(translationX: 0, y: 0)
                                },
                        completion: { finished in
                                src.present(dst, animated: false, completion: nil)
                                    }
                        )
    }
}


func bulletinDataToarticleData(data: bulletinArticleData) -> articleData{
    var temp = articleData();
    temp.articleAuthor = nil;
    temp.articleBody = data.articleBody;
    temp.articleUnixEpoch = data.articleUnixEpoch;
    temp.articleID = data.articleID;
    temp.articleImages = [];
    temp.articleTitle = data.articleTitle;
    temp.articleCatagory = data.articleCatagory;
    temp.hasHTML = data.hasHTML;
    return temp;
}

func resetAllSettingsDefaults() {
    let defaults = UserDefaults.standard
    let dictionary = defaults.dictionaryRepresentation()
    dictionary.keys.forEach { key in
        defaults.removeObject(forKey: key)
    }
}

// func that returns UIcolor from rgb values
func makeColor(r: Float, g: Float, b: Float) -> UIColor{
    return UIColor.init(red: CGFloat(r/255.0), green: CGFloat(g/255.0), blue: CGFloat(b/255.0), alpha: CGFloat(1.0));
}

func printFontFamilies(){
    for family in UIFont.familyNames {

        let sName: String = family as String
        print("family: \(sName)")
                
        for name in UIFont.fontNames(forFamilyName: sName) {
            print("name: \(name as String)")
        }
    }
}

final class InsetLabel: UILabel {
    override func drawText(in rect: CGRect) {
        super.drawText(in: rect.inset(by: UIEdgeInsets(top: 0, left: 20, bottom: 0, right: 20)))
    }
}

var utilNeedID: String?;

func findArticleFromIDAndSegue(id: String){ // performs segue as well
    if (id == ""){
        return;
    }
    utilNeedID = id;
    if (homeArticleList[0].count == 0 && homeArticleList[1].count == 0 && homeArticleList[2].count == 0){ // load homepage
        setUpConnection();
        if (internetConnected){
            homeArticleList = [[articleData]](repeating: [articleData](), count: 3);
            for i in 0...2{
                var s: String; // path inside homepage
                switch i {
                case 0: // general info
                    s = "General_Info";
                    break;
                case 1: // district
                    s = "District";
                    break;
                case 2: // asb
                    s = "ASB";
                    break;
                default:
                    s = "";
                    break;
                }
                
                ref.child("homepage").child(s).observeSingleEvent(of: .value) { (snapshot) in
                    let enumerator = snapshot.children;
                    var temp = [articleData](); // temporary array
                    while let article = enumerator.nextObject() as? DataSnapshot{ // each article
                        let enumerator = article.children;
                        var singleArticle = articleData();
                        
                        singleArticle.articleID = article.key;
                        
                        while let articleContent = enumerator.nextObject() as? DataSnapshot{ // data inside article
                        
                            
                            if (articleContent.key == "articleAuthor"){
                                singleArticle.articleAuthor = articleContent.value as? String;
                            }
                            else if (articleContent.key == "articleBody"){
                                singleArticle.articleBody = articleContent.value as? String;
                            }
                            else if (articleContent.key == "articleUnixEpoch"){
                                singleArticle.articleUnixEpoch = articleContent.value as? Int64;
                            }
                            else if (articleContent.key == "articleImages"){
                                
                                var tempImage = [String]();
                                let imageIt = articleContent.children;
                                while let image = imageIt.nextObject() as? DataSnapshot{
                                    tempImage.append(image.value as! String);
                                }
                                singleArticle.articleImages = tempImage;
                            }
                            else if (articleContent.key == "articleVideoIDs"){
                                var tempArr = [String]();
                                let idIt = articleContent.children;
                                while let id = idIt.nextObject() as? DataSnapshot{
                                    tempArr.append(id.value as! String);
                                }
                                singleArticle.articleVideoIDs = tempArr;
                            }
                            else if (articleContent.key == "articleTitle"){
                                
                                singleArticle.articleTitle = articleContent.value as? String;
                            }
                            else if (articleContent.key == "isFeatured"){
                                singleArticle.isFeatured = (articleContent.value as? Int == 0 ? false : true);
                            }
                            else if (articleContent.key == "hasHTML"){
                                singleArticle.hasHTML = (articleContent.value as? Int == 0 ? false : true);
                            }
                            
                            
                        }
                        singleArticle.articleCatagory = i == 0 ? "General Info" : s;
                        temp.append(singleArticle);
                        if (utilNeedID == singleArticle.articleID){
                            let articleDataDict: [String: articleData] = ["articleContent" : singleArticle];
                            NotificationCenter.default.post(name: NSNotification.Name(rawValue: "article"), object: nil, userInfo: articleDataDict);
                        }
                    }
                    homeArticleList[i] = temp;
                };
            }
        }
    }
    else{
        for j in homeArticleList{
            for i in j{
                if (id == i.articleID){
                    let articleDataDict: [String: articleData] = ["articleContent" : i];
                    NotificationCenter.default.post(name: NSNotification.Name(rawValue: "article"), object: nil, userInfo: articleDataDict);
                    return;
                }
            }
        }
    }
    if (bulletinArticleList[0].count == 0 && bulletinArticleList[1].count == 0 && bulletinArticleList[2].count == 0 && bulletinArticleList[3].count == 0 && bulletinArticleList[4].count == 0 && bulletinArticleList[5].count == 0){ // load bulletin
        setUpConnection();
        if (internetConnected){
            bulletinArticleList = [[bulletinArticleData]](repeating: [bulletinArticleData](), count: 6);
            
            for i in 0...4{
                var s: String; // path inside homepage
                switch i {
                case 0: // seniors
                    s = "Academics";
                    break;
                case 1: // colleges
                    s = "Athletics";
                    break;
                case 2: // events
                    s = "Clubs";
                    break;
                case 3: // athletics
                    s = "Colleges";
                    break;
                case 4: // reference
                    s = "Reference";
                    break;
                default:
                    s = "";
                    break;
                }
                ref.child("bulletin").child(s).observeSingleEvent(of: .value) { (snapshot) in
                    let enumerator = snapshot.children;
                    var temp = [bulletinArticleData](); // temporary array
                    while let article = enumerator.nextObject() as? DataSnapshot{ // each article
                        let enumerator = article.children;
                        var singleArticle = bulletinArticleData();
                        singleArticle.articleID = article.key;
                        while let articleContent = enumerator.nextObject() as? DataSnapshot{ // data inside article
                            
                            if (articleContent.key == "articleBody"){
                                singleArticle.articleBody = articleContent.value as? String;
                            }
                            else if (articleContent.key == "articleUnixEpoch"){
                                singleArticle.articleUnixEpoch = articleContent.value as? Int64;
                            }
                            else if (articleContent.key == "articleTitle"){
                                singleArticle.articleTitle = articleContent.value as? String;
                            }
                            else if (articleContent.key == "hasHTML"){
                                singleArticle.hasHTML = (articleContent.value as? Int == 0 ? false : true);
                            }
                            
                        }
                        singleArticle.articleCatagory = s;
                        singleArticle.articleType = i;
                        temp.append(singleArticle);
                        if (utilNeedID == singleArticle.articleID){
                            let articleDataDict: [String: articleData] = ["articleContent" : bulletinDataToarticleData(data: singleArticle)];
                            NotificationCenter.default.post(name: NSNotification.Name(rawValue: "article"), object: nil, userInfo: articleDataDict);
                        }
                    }
                    bulletinArticleList[i] = temp;
                };
                
            }
        }
    }
    else{
        for j in bulletinArticleList{
            for i in j{
                if (id == i.articleID){
                    let articleDataDict: [String: articleData] = ["articleContent" : bulletinDataToarticleData(data: i)];
                    NotificationCenter.default.post(name: NSNotification.Name(rawValue: "article"), object: nil, userInfo: articleDataDict);
                    return;
                }
            }
        }
    }
}

func updateSubscriptionNotifs(){
    let topics = ["general", "asb", "district", "bulletin"];
    if (selectedNotifications[0] == true){
        for topic in topics{
            Messaging.messaging().subscribe(toTopic: topic);
        }
    }
    else{
        for i in 1...4{
            if (selectedNotifications[i] == true){
                Messaging.messaging().subscribe(toTopic: topics[i-1]);
            }
            else{
                Messaging.messaging().unsubscribe(fromTopic: topics[i-1]);
            }
        }
    }
    Messaging.messaging().subscribe(toTopic: "mandatory");
}

let mainThemeColor = makeColor(r: 159, g: 12, b: 12);

var fontSize = 20;
