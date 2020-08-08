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

func getNotificationData(){
    setUpConnection();
    if (internetConnected){
        print("ok -------- loading articles - notifications");
        //print(s);
        totalNotificationList = [notificationData]();
        ref.child("notifications").observeSingleEvent(of: .value) { (snapshot) in
            let enumerator = snapshot.children;
            while let article = enumerator.nextObject() as? DataSnapshot{ // each article
                let enumerator = article.children;
                var singleNotification = notificationData();
                
                singleNotification.messageID = article.key;
                
                while let notificationContent = enumerator.nextObject() as? DataSnapshot{ // data inside article
                    
                    if (notificationContent.key == "notificationArticleID"){
                        singleNotification.notificationArticleID  = notificationContent.value as? String;
                    }
                    else if (notificationContent.key == "notificationBody"){
                        singleNotification.notificationBody  = notificationContent.value as? String;
                    }
                    else if (notificationContent.key == "notificationTitle"){
                        singleNotification.notificationTitle  = notificationContent.value as? String;
                    }
                    else if (notificationContent.key == "notificationUnixEpoch"){
                        singleNotification.notificationUnixEpoch  = notificationContent.value as? Int64;
                    }
                    else if (notificationContent.key == "notificationCategory"){
                        singleNotification.notificationCatagory = notificationContent.value as? Int;
                    }
                    
                }
                totalNotificationList.append(singleNotification);
                filterTotalNotificationArticles();
            };
        }
    }
    else{
        //setUpAllViews();
        print("no network detected - notifications");
    }
}

func loadNotifPref(){
    if let data = UserDefaults.standard.data(forKey: "notificationReadDict"){
        do{
            let decoder = JSONDecoder();
            notificationReadDict = try decoder.decode([String:Bool].self, from: data);
        }catch{
            print("error decoding")
        }
    }
    else{
        print("default - no saved found");
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
