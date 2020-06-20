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

var resetUpArticles = false;
var unreadNotif = false;

// swift file with shared functions and extensions between files

struct articleData: Codable {
    var articleID: String?;
    var articleTitle: String?;
    var articleUnixEpoch: Int64?; // unix epoch time stamp
    var articleBody: String?;
    var articleAuthor: String?;
    var articleImages: [String]?; // list of image urls
    var isFeatured = false;
}
var internetConnected = false;
var homeArticleList = [[articleData]](); // size of 4 rows, featured, asb, sports, district
var bulletinArticleList = [[bulletinArticleData]](); // size of 6 rows, seniors, colleges, events, athletics, reference, and others

struct notificationData: Codable{
    var messageID: String?;
    var notificationTitle: String?;
    var notificationBody: String?;
    var notificationUnixEpoch: Int64?;
    var notificationArticleID: String?; // articleID pointer
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
    var articleAuthor: String?;
    var articleImages: [String]?; // list of image urls
    var articleType = -1;
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


public class Reachability {

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



class SegueFromRight: UIStoryboardSegue {
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
    temp.articleAuthor = data.articleAuthor;
    temp.articleBody = data.articleBody;
    temp.articleUnixEpoch = data.articleUnixEpoch;
    temp.articleID = data.articleID;
    temp.articleImages = data.articleImages;
    temp.articleTitle = data.articleTitle;
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

let mainThemeColor = makeColor(r: 153, g: 41, b: 56);

var fontSize = 20;
