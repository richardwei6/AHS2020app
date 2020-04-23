//
//  bulletinPage.swift
//  AHS20
//
//  Created by Richard Wei on 4/5/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit

class bulletinClass: UIViewController, UIScrollViewDelegate, UITabBarControllerDelegate {

    @IBOutlet weak var filterScrollView: UIScrollView!
    @IBOutlet weak var bulletinScrollView: UIScrollView!
    
    @IBOutlet weak var monthLabel: UILabel!
    
    // padding variables
    let iconHorizontalPadding = CGFloat(10);
    let articleHorizontalPadding = CGFloat(10);
    let articleVerticalPadding = CGFloat(10);
    let articleVerticalSize = CGFloat(100);
    
    
    let filterSize = 6;
    var filterFrame = CGRect(x:0,y:0,width: 0, height: 0);
    var filterIconSize = CGFloat(85);
    
    
    var bulletinSize = 10;
    var bulletinFrame = CGRect(x:0, y:0, width: 0, height: 0);
    
    @objc func openArticle(){
        print("Button pressed");
        performSegue(withIdentifier: "articleSegue", sender: nil);
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad();
       
        filterIconSize = filterScrollView.frame.size.height;
        
        let month = Calendar.current.component(.month, from: Date());
        monthLabel.text = "Month " + (month < 10 ? "0":"") + String(month);
        
        // set up both scrollviews here
        
        
        
        //filterFrame.size = filterScrollView.frame.size;
        filterFrame.size.height = filterIconSize;
        filterFrame.size.width = filterIconSize; //
        
        for buttonIndex in 0..<filterSize{
            filterFrame.origin.x = articleHorizontalPadding+((filterIconSize+iconHorizontalPadding) * CGFloat(buttonIndex));
            let buttonView = UIButton(frame: filterFrame);
            //buttonView.setImage("", for: .normal);
            buttonView.backgroundColor = UIColor.gray;
            
            self.filterScrollView.addSubview(buttonView);
        }
        filterScrollView.contentSize = CGSize(width: (filterIconSize+iconHorizontalPadding) * CGFloat(filterSize), height: filterScrollView.frame.size.height);
        filterScrollView.delegate = self;
        
        
        // set up bulletin
        bulletinFrame.size.height = articleVerticalSize;
        bulletinFrame.size.width = UIScreen.main.bounds.size.width - (2*articleHorizontalPadding);
        
        for aIndex in 0..<bulletinSize{
            bulletinFrame.origin.x = articleHorizontalPadding;
            bulletinFrame.origin.y = ((bulletinFrame.size.height+articleVerticalPadding)*CGFloat(aIndex));
            let articleButton = UIButton(frame: bulletinFrame);
            articleButton.backgroundColor = UIColor.gray;
            
            
            
            articleButton.addTarget(self, action: #selector(self.openArticle), for: .touchUpInside)
            self.bulletinScrollView.addSubview(articleButton);
        }
        bulletinScrollView.contentSize = CGSize(width: bulletinFrame.size.width-(2*articleHorizontalPadding), height: articleHorizontalPadding+(bulletinFrame.size.height+articleHorizontalPadding)*CGFloat(bulletinSize));
        bulletinScrollView.delegate = self;
    }

}