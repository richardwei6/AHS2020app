//
//  settingPage.swift
//  AHS20
//
//  Created by Richard Wei on 4/5/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit
import AudioToolbox

class settingClass: UITableViewController {
    
    
    
    @IBOutlet weak var fontWordLabel: UILabel!
    @IBOutlet weak var fontSizeSlider: UISlider!
    @IBOutlet weak var fontSizeLabel: UILabel!
    @IBOutlet weak var appVersionLabel: UILabel!
    
    @IBOutlet weak var clearSavedArticleCell: UITableViewCell!
    
    override func viewDidLoad() {
        super.viewDidLoad();
        fontSizeSlider.value = Float(fontSize);
        fontSizeLabel.text = String(fontSize);
        appVersionLabel.text = (Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String ?? "1.0") + "." +  (Bundle.main.infoDictionary?["CFBundleVersion"] as? String ?? "0");
        updateWordLabel();
    }
    
    func updateWordLabel(){
        if (fontSize == 20){
            fontWordLabel.text = "Default";
            fontWordLabel.font = UIFont(name: fontWordLabel.font.fontName, size: max(CGFloat(fontSize-3), 1));
            fontWordLabel.textColor = UIColor.black;
        }
        else if (fontSize >= 1 && fontSize < 20){
            fontWordLabel.text = "Small";
            fontWordLabel.font = UIFont(name: fontWordLabel.font.fontName, size: max(CGFloat(fontSize-3), 1));
            fontWordLabel.textColor = UIColor.systemBlue;
        }
        else if (fontSize > 20 && fontSize <= 40){
            fontWordLabel.text = "Large";
            fontWordLabel.font = UIFont(name: fontWordLabel.font.fontName, size: max(CGFloat(fontSize-3), 1));
            fontWordLabel.textColor = UIColor.brown;
        }
        else{
            fontWordLabel.text = "MASSIVE";
            fontWordLabel.font = UIFont(name: fontWordLabel.font.fontName, size: min(CGFloat(fontSize-3), 44));
            fontWordLabel.textColor = mainThemeColor;
        }
    }
    
    @IBAction func sliderChange(_ sender: Any) {
        fontSize = Int(fontSizeSlider.value);
        fontSizeLabel.text = String(fontSize);
        UserDefaults.standard.set(fontSize, forKey: "fontSize");
        updateWordLabel();
    }
    
    @IBAction func resetPreferences(_ sender: Any) {
        AudioServicesPlaySystemSound(1519);
        let confirmPopup = UIAlertController(title: "Reset saved articles", message: "Your saved articles will be removed", preferredStyle: UIAlertController.Style.actionSheet);
        confirmPopup.addAction(UIAlertAction(title: "Ok", style: .destructive, handler: { (action: UIAlertAction!) in
            UserDefaults.standard.removeObject(forKey: "savedArticleDict");
            savedArticleClass.savedArticles = [String: articleData]();
            resetUpArticles = true;
            resetAllSettingsDefaults();
        }));
        confirmPopup.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: { (action: UIAlertAction!) in }));
        present(confirmPopup, animated: true, completion: nil);
    }
    
    
}
