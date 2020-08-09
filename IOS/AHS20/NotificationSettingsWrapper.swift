//
//  NotificationSettingsWrapper.swift
//  AHS20
//
//  Created by Richard Wei on 5/31/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit
import AudioToolbox

class notificationSettingsWrapperClass: UIViewController{
    
    @IBOutlet weak var notificationsContainer: UIView!
    
    @IBAction func dismiss(_ sender: Any) {
        dismiss(animated: true);
        UIImpactFeedbackGenerator(style: .light).impactOccurred();
    }
    override func viewDidLoad(){
        super.viewDidLoad();
        notificationsContainer.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;
    }
    
}
