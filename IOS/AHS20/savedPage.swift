//
//  savedPage.swift
//  AHS20
//
//  Created by Richard Wei on 4/5/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit

class savedClass: UIViewController, UIScrollViewDelegate, UITabBarControllerDelegate {

    
    @IBOutlet weak var monthLabel: UILabel!
    
    @IBOutlet weak var notificationBellButton: UIButton!
    
    @objc func openNotifcations(sender: UIButton){
        print("Notifcations");
        performSegue(withIdentifier: "notificationSegue", sender: nil);
    }
    
    override func viewDidLoad() {
        super.viewDidLoad();
        
        monthLabel.text = getTitleDateAndMonth();
        monthLabel.adjustsFontSizeToFitWidth = true;
        monthLabel.minimumScaleFactor = 0.8;
        
        notificationBellButton.addTarget(self, action: #selector(self.openNotifcations), for: .touchUpInside);
        
    }

}
