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

    override func viewDidLoad() {
        super.viewDidLoad();
    }

    @IBAction func test(_ sender: Any) {
        performSegue(withIdentifier: "notificationToArticle", sender: nil);
    }
    @IBAction func exitPopup(_ sender: UIButton) {
         dismiss(animated: true);
    }
}
