//
//  epochClass.swift
//  AHS20
//
//  Created by Richard Wei on 6/20/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit
import AudioToolbox
import SystemConfiguration
import FirebaseDatabase
import SDWebImage

final class epochClass{
    final class func epochToString(epoch: Int64) -> String{ // 1 hour ago
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
    
    final class func epochToDateString(epoch: Int64) -> String{ // 99/99/99
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
    
    final class func epochToFormatedDateString(epoch: Int64) -> String{ // Month 00, 2000
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
