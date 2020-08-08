//
//  notificationFunc.swift
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


// NOTIFICATION START
var totalNotificationList = [notificationData]();
var notificationReadDict = [String : Bool](); // Message ID : Read = true
var notificationList = [[notificationData]](repeating: [notificationData](), count: 2);

// notification settings
var selectedNotifications = [Bool](repeating: false, count: 5); // true or false 0 - 4 0 is general

func loadNotifPref(){
    if let data = UserDefaults.standard.data(forKey: "notificationReadDict"){
        do{
            let decoder = JSONDecoder();
            notificationReadDict = try decoder.decode([String:Bool].self, from: data);
        }catch{
            print("error decoding")
        }
    }
}

func saveNotifPref(){
    do{
        
        var currDict = [String: Bool]();
        for i in totalNotificationList{
            currDict[i.messageID ?? ""] = true;
        }
        
        var save = [String: Bool](); // messageid
        for i in notificationReadDict{
            if (currDict[i.key] == true){
                save[i.key] = true;
            }
        }
        
        // combine only stuff that exists
        let encoder = JSONEncoder();
        let data = try encoder.encode(save);
        UserDefaults.standard.set(data, forKey: "notificationReadDict");
    } catch{
        print("error encoding object to save");
    }
}

func filterTotalNotificationArticles(){ // inital
    loadNotifPref();
    notificationList = [[notificationData]](repeating: [notificationData](), count: 2);
    for notification in totalNotificationList{
        if (notification.notificationCatagory == 0 || selectedNotifications[notification.notificationCatagory ?? 0] == true || selectedNotifications[0] == true){
            notificationList[(notificationReadDict[notification.messageID ?? ""] == true ? 0 : 1)].append(notification);
        }
    }
  //  print("change - \(notificationList)")
    unreadNotif = (notificationList[1].count > 0);
}

// NOTIFICATION END
