//
//  File.swift
//  AHS20
//
//  Created by Richard Wei on 5/7/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit

class notificationSettingsClass: UITableViewController{
    
    @IBOutlet weak var generalUpdates: UISwitch!
    
    @IBOutlet weak var sportsNews: UISwitch!
    @IBOutlet weak var asbNews: UISwitch!
    @IBOutlet weak var districtNews: UISwitch!
    @IBOutlet weak var bulletinNews: UISwitch!
    
    
    
    override func viewDidLoad(){
        super.viewDidLoad();
        for i in 0...4{
            switch i {
            case 0:
                if (selectedNotifications[i] == true){
                    generalUpdates.isOn = true;
                }
            case 1:
                if (selectedNotifications[i] == true){
                    sportsNews.isOn = true;
                }
            case 2:
                if (selectedNotifications[i] == true){
                    asbNews.isOn = true;
                }
            case 3:
                if (selectedNotifications[i] == true){
                    districtNews.isOn = true;
                }
            case 4:
                if (selectedNotifications[i] == true){
                    bulletinNews.isOn = true;
                }
            default:
                break;
            }
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        
        for i in 0...4{
            switch i {
            case 0:
                if (selectedNotifications[i] == true){
                    generalUpdates.isOn = true;
                }
            case 1:
                if (selectedNotifications[i] == true){
                    sportsNews.isOn = true;
                }
            case 2:
                if (selectedNotifications[i] == true){
                    asbNews.isOn = true;
                }
            case 3:
                if (selectedNotifications[i] == true){
                    districtNews.isOn = true;
                }
            case 4:
                if (selectedNotifications[i] == true){
                    bulletinNews.isOn = true;
                }
            default:
                break;
            }
        }
    }
    
    
    @IBAction func changedSettings(_ sender: UISwitch) {
        if (sender.tag > 0){
            if (generalUpdates.isOn == true){
                //print("false");
                generalUpdates.isOn = false;
                selectedNotifications[0] = false;
            }
            if (sender.isOn == true){
                selectedNotifications[sender.tag] = true;
            }
            else{
                selectedNotifications[sender.tag] = false;
            }
        }
        else{
            if (sender.isOn == true){
                sportsNews.isOn = false;
                asbNews.isOn = false;
                districtNews.isOn = false;
                bulletinNews.isOn = false;
                selectedNotifications = [Bool](repeating: false, count: 5);
                selectedNotifications[0] = true;
            }
            else{
                selectedNotifications[sender.tag] = false;
            }
        }
        
        print(selectedNotifications);
        UserDefaults.standard.set(selectedNotifications, forKey: "selectedNotifications");
        
        updateSubscriptionNotifs();
        
        filterTotalNotificationArticles();
        unreadNotif = (notificationList[1].count > 0);
    }
    
    @IBAction func dismiss(_ sender: Any){
        dismiss(animated: true);
        UIImpactFeedbackGenerator(style: .light).impactOccurred();
    }
    
}
