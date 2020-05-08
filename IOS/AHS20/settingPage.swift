//
//  settingPage.swift
//  AHS20
//
//  Created by Richard Wei on 4/5/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit

class settingClass: UITableViewController {

    

    @IBOutlet weak var fontSizeSlider: UISlider!
    @IBOutlet weak var fontSizeLabel: UILabel!
    
    @IBOutlet weak var clearSavedArticleCell: UITableViewCell!
    
    override func viewDidLoad() {
        super.viewDidLoad();
        fontSizeSlider.value = Float(fontSize);
        fontSizeLabel.text = String(fontSize);
    }
    
    @IBAction func sliderChange(_ sender: Any) {
        fontSize = Int(fontSizeSlider.value);
        fontSizeLabel.text = String(fontSize);
        UserDefaults.standard.set(fontSize, forKey: "fontSize");
    }
    
    @IBAction func resetPreferences(_ sender: Any) {
        UserDefaults.standard.removeObject(forKey: "savedArticles");
        savedArticles = [];
    }
}
