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
    
    var selectedNotifications = [Int]();
    
    
    override func viewDidLoad(){
        super.viewDidLoad();
    }
    
    override func viewDidAppear(_ animated: Bool) {
        selectedNotifications = UserDefaults.standard.array(forKey: "selectedNotifications") as? [Int] ?? [0];
        
        for i in selectedNotifications{
            switch i {
            case 0:
                generalUpdates.isOn = true;
                //break;
            case 1:
                sportsNews.isOn = true;
            case 2:
                asbNews.isOn = true;
            case 3:
                districtNews.isOn = true;
            case 4:
                bulletinNews.isOn = true;
            default:
                break;
            }
        }
    }
    
    func remove(element: Int){
        if (selectedNotifications.firstIndex(of: element) != nil){
            selectedNotifications.remove(at: selectedNotifications.firstIndex(of: element)!);
        }
    }
    
    @IBAction func changedSettings(_ sender: UISwitch) {
        if (sender.tag > 0){
            if (generalUpdates.isOn == true){
                //print("false");
                generalUpdates.isOn = false;
                remove(element: 0);
            }
            if (sender.isOn == true){
                selectedNotifications.append(sender.tag);
            }
            else{
                remove(element: sender.tag);
            }
        }
        else{
            if (sender.isOn == true){
                sportsNews.isOn = false;
                asbNews.isOn = false;
                districtNews.isOn = false;
                bulletinNews.isOn = false;
                selectedNotifications = [0];
            }
            else{
                remove(element: sender.tag);
            }
        }
   
        print(selectedNotifications);
        UserDefaults.standard.set(selectedNotifications, forKey: "selectedNotifications");
    }
    
    @IBAction func dismiss(_ sender: Any){
        dismiss(animated: true);
    }
    
}
