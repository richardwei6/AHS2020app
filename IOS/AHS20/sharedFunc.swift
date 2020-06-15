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
var bulletinArticleList = [[bulletinClass.bulletinArticleData]](); // size of 6 rows, seniors, colleges, events, athletics, reference, and others

struct notificationData: Codable{
    var messageID: String?;
    var notificationTitle: String?;
    var notificationBody: String?;
    var notificationUnixEpoch: Int64?;
    var notificationArticleID: String?; // articleID pointer
}

var notificationList = [[notificationData]](repeating: [notificationData](), count: 2); // 0 is read notifications, 1 is unread

func loadNotificationPref(){
    if let data = UserDefaults.standard.data(forKey: "notificationList"){
        do{
            let decoder = JSONDecoder();
            
            notificationList = try decoder.decode([[notificationData]].self, from: data);
            
        }catch{
            print("error decoding")
        }
    }
    else{
        print("default - no saved found");
    }
}

func saveNotificationPref(){
    do{
        let encoder = JSONEncoder();
        let data = try encoder.encode(notificationList);
        UserDefaults.standard.set(data, forKey: "notificationList");
    } catch{
        print("error encoding object to save");
    }
}

class savedArticleClass{
    static var savedArticles = [String: articleData]();
    class func getSavedArticles() -> [articleData]{ // get saved articles from phone local storage
        //savedArticles = ( UserDefaults.standard.dictionary(forKey: "savedArticles") ?? [String: articleData]() ) as! [String : articleData];
        getArticleDictionary();
        var temp = [articleData]();
        for i in savedArticles{
            temp.append(i.value);
        }
        return temp;
    }
    class func saveCurrArticle(articleID: String, article: articleData){
        savedArticles[articleID] = article;
        saveArticleDictionary();
    }
    class func removeCurrArticle(articleID: String){
        savedArticles[articleID] = nil;
        saveArticleDictionary();
    }
    
    class func isSavedCurrentArticle(articleID: String) -> Bool{
        return savedArticles[articleID] != nil;
    }
    // custom object saving
    class func saveArticleDictionary(){
        do{
            let encoder = JSONEncoder();
            let data = try encoder.encode(savedArticles);
            UserDefaults.standard.set(data, forKey: "savedArticleDict");
        } catch{
            print("error encoding object to save");
        }
    }
    class func getArticleDictionary(){
        if let data = UserDefaults.standard.data(forKey: "savedArticleDict"){
            do{
                let decoder = JSONDecoder();
                
                savedArticles = try decoder.decode([String:articleData].self, from: data);
                
            }catch{
                print("error decoding")
            }
        }
        else{
            print("default - no saved found");
        }
    }
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




class CustomUIButton: UIButton{
    var articleIndex = -1;
    var articleCompleteData = articleData();
}

class notificationUIButton: CustomUIButton{
    var unreadBool = false;
}

func getTitleDateAndMonth() -> String {
    let dateObj = Date();
    let calender = Calendar.current;
    let dayInt = calender.component(.day , from: dateObj);
    
    let monthInt = Calendar.current.dateComponents([.month], from: Date()).month;
    let monthStr = Calendar.current.monthSymbols[monthInt!-1];
    return String(monthStr) + " " + String(dayInt);
}

class epochClass{
    class func epochToString(epoch: Int64) -> String{ // 1 hour ago
        if (epoch == -1){
            return "NULL";
        }
        let currTime = Int64(NSDate().timeIntervalSince1970);
        let diff = currTime - epoch;
        let timePattern = [(1, "second"), (60, "minute"), (3600, "hour"), (86400, "day"), (604800, "week"), (2592000, "month"), (31536000, "year")];
        var r = "NULL";
        if (diff >= 0){
            for i in 1...6{
                if (floor(Double(diff) / Double(timePattern[i].0)) == 0){
                    let prefix = Int(floor(Double(diff) / Double(timePattern[i-1].0)));
                    r = "\(prefix) " + timePattern[i-1].1 + (prefix > 1 ? "s" : "") + " ago";
                    break;
                }
            }
        }
        return r;
    }
    
    class func epochToDateString(epoch: Int64) -> String{ // 99/99/99
        if (epoch == -1){
            return "NULL";
        }
        let date = Date(timeIntervalSince1970: TimeInterval(epoch));
        let cal = Calendar.current;
        let year = cal.component(.year, from: date);
        let month = cal.component(.month, from: date);
        let day = cal.component(.day, from: date);
        return "\(month)/\(day)/\(year)";
    }
    
    class func epochToFormatedDateString(epoch: Int64) -> String{ // Month 00, 2000
        if (epoch == -1){
            return "NULL";
        }
        let date = Date(timeIntervalSince1970: TimeInterval(epoch));
        let cal = Calendar.current;
        let year = cal.component(.year, from: date);
        let month = cal.component(.month, from: date);
        let day = cal.component(.day, from: date);
        let monthStr = Calendar.current.monthSymbols[month-1];
        return "\(monthStr) \(day), \(year)";
    }
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

        /* Only Working for WIFI
        let isReachable = flags == .reachable
        let needsConnection = flags == .connectionRequired

        return isReachable && !needsConnection
        */

        // Working for Cellular and WIFI
        let isReachable = (flags.rawValue & UInt32(kSCNetworkFlagsReachable)) != 0
        let needsConnection = (flags.rawValue & UInt32(kSCNetworkFlagsConnectionRequired)) != 0

        return (isReachable && !needsConnection)

    }
}

extension UILabel {

    // Pass value for any one of both parameters and see result
    func setLineSpacing(lineSpacing: CGFloat = 0.0, lineHeightMultiple: CGFloat = 0.0) {

        guard let labelText = self.text else { return }

        let paragraphStyle = NSMutableParagraphStyle()
        paragraphStyle.lineSpacing = lineSpacing
        paragraphStyle.lineHeightMultiple = lineHeightMultiple

        let attributedString:NSMutableAttributedString
        if let labelattributedText = self.attributedText {
            attributedString = NSMutableAttributedString(attributedString: labelattributedText)
        } else {
            attributedString = NSMutableAttributedString(string: labelText)
        }

        // Line spacing attribute
        attributedString.addAttribute(NSAttributedString.Key.paragraphStyle, value:paragraphStyle, range:NSMakeRange(0, attributedString.length))

        self.attributedText = attributedString
    }
}

extension UILabel{ // add setRoundedEdge func to UILabel
    func setRoundedEdge(corners:UIRectCorner, radius: CGFloat){ // label.setRoundedEdge([.TopLeft, . TopRight], radius: 10)
        let path = UIBezierPath(roundedRect: bounds, byRoundingCorners: corners, cornerRadii: CGSize(width: radius, height: radius))
        let mask = CAShapeLayer()
        mask.path = path.cgPath
        layer.mask = mask
    }
}

extension UIButton{
    func setRoundedEdge(corners:UIRectCorner, radius: CGFloat){ // label.setRoundedEdge([.TopLeft, . TopRight], radius: 10)
        let path = UIBezierPath(roundedRect: bounds, byRoundingCorners: corners, cornerRadii: CGSize(width: radius, height: radius))
        let mask = CAShapeLayer()
        mask.path = path.cgPath
        layer.mask = mask
    }
}

extension UIImageView {
    func setImageColor(color: UIColor) {
        let templateImage = self.image?.withRenderingMode(.alwaysTemplate)
        self.image = templateImage
        self.tintColor = color
    }
    func imgFromURL(sURL: String) {
        if (sURL == ""){
            return;
        }
        self.sd_setImage(with: URL(string: sURL));
    }
    func setRoundedEdge(corners:UIRectCorner, radius: CGFloat){ // label.setRoundedEdge([.TopLeft, . TopRight], radius: 10)
        let path = UIBezierPath(roundedRect: bounds, byRoundingCorners: corners, cornerRadii: CGSize(width: radius, height: radius))
        let mask = CAShapeLayer()
        mask.path = path.cgPath
        layer.mask = mask
    }
}

extension UIImage {
    
    func maskWithColor(color: UIColor) -> UIImage? {
        let maskImage = cgImage!

        let width = size.width
        let height = size.height
        let bounds = CGRect(x: 0, y: 0, width: width, height: height)

        let colorSpace = CGColorSpaceCreateDeviceRGB()
        let bitmapInfo = CGBitmapInfo(rawValue: CGImageAlphaInfo.premultipliedLast.rawValue)
        let context = CGContext(data: nil, width: Int(width), height: Int(height), bitsPerComponent: 8, bytesPerRow: 0, space: colorSpace, bitmapInfo: bitmapInfo.rawValue)!

        context.clip(to: bounds, mask: maskImage)
        context.setFillColor(color.cgColor)
        context.fill(bounds)

        if let cgImage = context.makeImage() {
            let coloredImage = UIImage(cgImage: cgImage)
            return coloredImage
        } else {
            return nil
        }
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
