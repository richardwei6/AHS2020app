//
//  ViewController.swift
//  AHS20
//
//  Created by Richard Wei on 3/14/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import UIKit

class ViewController: UIViewController, UIScrollViewDelegate {

    // link UI elements to swift via outlets
    @IBOutlet weak var mainPageScrollView: UIScrollView!
    @IBOutlet weak var ausdNewsScrollView: UIScrollView!
    @IBOutlet weak var ausdNewsPageControl: UIPageControl!
    @IBOutlet weak var clubScrollView: UIScrollView!
    @IBOutlet weak var asbScrollView: UIScrollView!
    @IBOutlet weak var otherScrollView: UIScrollView!
    @IBOutlet weak var sportsScrollView: UIScrollView!
    @IBOutlet weak var sportsPageControl: UIPageControl!
    
    
    // 4 is default and 0-3 strings are default
    //var ausdNewsArr: [String] = ["0","1","2","3"]
    var ausdNewsSize = 10
    var ausdNewsframe = CGRect(x:0,y:0,width:0,height:0)
    
    var clubSize = 4
    var clubframe = CGRect(x:0,y:0,width:0,height:0)
    
    var asbSize = 4
    var asbframe = CGRect(x:0,y:0,width:0,height:0)
    
    var otherSize = 4
    var otherframe = CGRect(x:0,y:0,width:0,height:0)
    
    var sportsSize = 4
    var sportsframe = CGRect(x:0,y:0,width:0,height:0)
    
    // func that returns UIcolor from rgb values
    
    func makeColor(r: Float, g: Float, b: Float) -> UIColor{
        return UIColor.init(red: CGFloat(r/255.0), green: CGFloat(g/255.0), blue: CGFloat(b/255.0), alpha: CGFloat(1.0));
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        
        
        // ausdnews - 4 is default
        ausdNewsPageControl.numberOfPages = ausdNewsSize;
        ausdNewsScrollView.layer.cornerRadius = 20;
        sportsPageControl.layer.borderColor = UIColor.white.cgColor;
        for articleIndex in 0..<ausdNewsSize {
            // set up frame
            ausdNewsframe.origin.x = ausdNewsScrollView.frame.size.width * CGFloat(articleIndex);
            ausdNewsframe.size = ausdNewsScrollView.frame.size;
            // set up text onto scrollview
            // TODO: implement array that has needed text from firebase
            
            let contentView = UIView(frame: ausdNewsframe); // content view will contain text and image
            contentView.backgroundColor = makeColor(r: 147, g: 66, b: 78);
            
            let textFrame = CGRect(x: 0.0, y: 0.0, width: 200.0, height: 30.0); // pos, width, and height of text
            let textView = UITextView(frame: textFrame);
            textView.text = "Sample text with image";
            //textView.backgroundColor = makeColor(r: 147, g: 66, b: 78);
            
            let imageFrame = CGRect(x: 50.0, y: 50.0, width: 100.0, height: 100.0);
            let imageview = UIImageView(frame: imageFrame);
            imageview.image = UIImage(named: "Asset 14");
            
            contentView.addSubview(textView);
            contentView.addSubview(imageview);
            
            self.ausdNewsScrollView.addSubview(contentView);
        }
        // set size of scrollview
        ausdNewsScrollView.contentSize = CGSize(width: (ausdNewsScrollView.frame.size.width * CGFloat(ausdNewsSize)), height: ausdNewsScrollView.frame.size.height);
        ausdNewsScrollView.delegate = self;
        
        
        
        
        
        // clubs
        for pageIndex in 0..<clubSize {
            // set up frame
            clubframe.origin.x = clubScrollView.frame.size.width * CGFloat(pageIndex);
            clubframe.size = clubScrollView.frame.size;
            
            // content goes here:
            // TODO: implement array that has needed text from firebase
            let contentView = UIView(frame: clubframe); // content view will contain text and image
            //contentView.backgroundColor = makeColor(r: 0, g: 255, b: 255);
            
            let textFrame = CGRect(x: 0.0, y: 0.0, width: 200.0, height: 30.0); // pos, width, and height of text
            let textView = UITextView(frame: textFrame);
            textView.text = "Sample text with image";
            //textView.backgroundColor = makeColor(r: 147, g: 66, b: 78);
            
            let imageFrame = CGRect(x: 50.0, y: 50.0, width: 100.0, height: 100.0);
            let imageview = UIImageView(frame: imageFrame);
            imageview.image = UIImage(named: "Asset 14");
            
            contentView.addSubview(textView);
            contentView.addSubview(imageview);
            
            self.clubScrollView.addSubview(contentView);
        }
        // set size of scrollview
        clubScrollView.contentSize = CGSize(width: (clubScrollView.frame.size.width * CGFloat(clubSize)), height: clubScrollView.frame.size.height);
        clubScrollView.delegate = self;
        
        
        
        
        //asb
        for pageIndex in 0..<asbSize {
            // set up frame
            asbframe.origin.x = asbScrollView.frame.size.width * CGFloat(pageIndex);
            asbframe.size = asbScrollView.frame.size;
            
            // content goes here:
            // TODO: implement array that has needed text from firebase
            let textView = UITextView(frame: asbframe);
            textView.text = "Sample text";
            textView.backgroundColor = makeColor(r: 0, g: 255, b: 255);
            
            
            self.asbScrollView.addSubview(textView);
        }
        // set size of scrollview
        asbScrollView.contentSize = CGSize(width: (asbScrollView.frame.size.width * CGFloat(asbSize)), height: asbScrollView.frame.size.height);
        asbScrollView.delegate = self;
        
        
        
        
        
        //other
        for pageIndex in 0..<otherSize {
            // set up frame
            otherframe.origin.x = otherScrollView.frame.size.width * CGFloat(pageIndex);
            otherframe.size = otherScrollView.frame.size;
            
            // content goes here:
            // TODO: implement array that has needed text from firebase
            let textView = UITextView(frame: otherframe);
            textView.text = "Sample text";
            textView.backgroundColor = makeColor(r: 0, g: 255, b: 0);
            //textView.backgroundColor = UIColor.init(red: CGFloat(147.0/255.0), green: CGFloat(66.0/255.0), blue: CGFloat(78.0/255.0), alpha: CGFloat(1.0));
            
            
            self.otherScrollView.addSubview(textView);
        }
        // set size of scrollview
        otherScrollView.contentSize = CGSize(width: (otherScrollView.frame.size.width * CGFloat(otherSize)), height: otherScrollView.frame.size.height);
        otherScrollView.delegate = self;
        
        
        
        //sports
        sportsPageControl.numberOfPages = sportsSize;
        for articleIndex in 0..<sportsSize {
            // set up frame
            sportsframe.origin.x = sportsScrollView.frame.size.width * CGFloat(articleIndex);
            sportsframe.size = sportsScrollView.frame.size;
            // set up text onto scrollview
            // TODO: implement array that has needed text from firebase
            let textView = UITextView(frame: sportsframe);
            textView.text = "Sample text in sports";
            textView.backgroundColor = makeColor(r: 147, g: 66, b: 78);
            self.sportsScrollView.addSubview(textView);
        }
        // set size of scrollview
        sportsScrollView.contentSize = CGSize(width: (sportsScrollView.frame.size.width * CGFloat(sportsSize)), height: sportsScrollView.frame.size.height);
        sportsScrollView.delegate = self;
        
        
    }
    // every time scrollview stops moving
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        // gets current page num
        // set dot of pagecontrol to correct page
        
        ausdNewsPageControl.currentPage = Int(ausdNewsScrollView.contentOffset.x / ausdNewsScrollView.frame.size.width); // Typecasting float to int
        
        sportsPageControl.currentPage = Int(sportsScrollView.contentOffset.x / sportsScrollView.frame.size.width); // Typecasting float to int
        
    }

}

