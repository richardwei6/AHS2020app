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

var resetUpArticles = false;

// swift file with shared functions and extensions between files


var savedArticles = [String]();

func getSavedArticles(){ // get saved articles from phone local storage
            //savedArticles = ["", "", "", "", "", "", ""];
    savedArticles = UserDefaults.standard.stringArray(forKey: "savedArticles") ?? [];
}

func saveCurrentArticle(articleID: String){
  //  if (savedArticles.contains(articleID) == false){ TODO: remove comments after implmenting article id
        savedArticles.append(articleID);
        UserDefaults.standard.set(savedArticles, forKey: "savedArticles");
    //}
}

func removeCurrentArticle(articleID: String){
    // TODO: add function to look for article id and remove it;
    UserDefaults.standard.set(savedArticles, forKey: "savedArticles");
}
    
func isSavedCurrentArticle(articleID: String) -> Bool{
    return false; // TODO: integrate function later
}



class article{
    struct articleData: Codable {
        var articleID: String?;
    }
    var articleList = [articleData]();
    class func setUpLocalData(){ // TODO: implement to got all data from json file and input to articleData
        
    }
}


class CustomUIButton: UIButton{
    var articleIndex = -1;
    var articleID: String? // is articleID a string?
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
