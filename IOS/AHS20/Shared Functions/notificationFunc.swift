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
//var notificationList = [[notificationData]](repeating: [notificationData](), count: 2);

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

func filterThroughSelectedNotifcations() -> [notificationData]{
     selectedNotifications = UserDefaults.standard.array(forKey: "selectedNotifications") as? [Bool] ?? [true, false, false, false, false];
     updateSubscriptionNotifs();
     var output = [notificationData]();
     for i in totalNotificationList{
        if (selectedNotifications[0] == true || selectedNotifications[i.notificationCatagory ?? 0] == true || i.notificationCatagory == 0){
             output.append(i);
         }
     }
     return output;
}

func numOfUnreadInArray(arr: [notificationData]) -> Int{
    loadNotifPref();
    selectedNotifications = UserDefaults.standard.array(forKey: "selectedNotifications") as? [Bool] ?? [true, false, false, false, false];
    updateSubscriptionNotifs();
    var count = 0;
    for singleNotification in arr{
        if ((selectedNotifications[0] == true || selectedNotifications[singleNotification.notificationCatagory ?? 0] == true || singleNotification.notificationCatagory == 0) && notificationReadDict[singleNotification.messageID ?? ""] != true){
            count += 1;
        }
    }
    return count;
}

// NOTIFICATION END
