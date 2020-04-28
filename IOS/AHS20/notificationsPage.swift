//
//  notificationsPage.swift
//  AHS20
//
//  Created by Richard Wei on 4/24/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit

class notificationsClass: UIViewController, UIScrollViewDelegate, UITabBarControllerDelegate {

    @IBOutlet weak var monthLabel: UILabel!
    @IBOutlet weak var notificationScrollViews: UIScrollView!
    
    @objc func openArticle(_ sender: Any) {
           performSegue(withIdentifier: "notificationToArticle", sender: nil);
    }
    
    override func viewDidLoad() {
        super.viewDidLoad();
        
        monthLabel.text = getTitleDateAndMonth();
        monthLabel.adjustsFontSizeToFitWidth = true;
        monthLabel.minimumScaleFactor = 0.8;
        
        
        
    }
    
    
    @IBAction func exitPopup(_ sender: UIButton) {
         dismiss(animated: true);
    }
    
}
