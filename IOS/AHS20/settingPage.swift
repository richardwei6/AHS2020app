//
//  settingPage.swift
//  AHS20
//
//  Created by Richard Wei on 4/5/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit

class settingClass: UITableViewController, UITabBarControllerDelegate {

    override func viewDidLoad() {
        super.viewDidLoad();
    }

    @IBAction func resetPreferences(_ sender: Any) {
        UserDefaults.standard.removeObject(forKey: "savedArticles");
        savedArticles = [];
    }
}
