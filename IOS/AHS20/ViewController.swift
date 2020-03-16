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
    
    
    // 4 is default and 0-3 strings are default
    //var ausdNewsArr: [String] = ["0","1","2","3"]
    var ausdNewsSize = 5
    var ausdNewsframe = CGRect(x:0,y:0,width:0,height:0)
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        // 4 pages is default
        ausdNewsPageControl.numberOfPages = ausdNewsSize;
        for articleIndex in 0..<ausdNewsSize {
            // set up frame
            ausdNewsframe.origin.x = ausdNewsScrollView.frame.size.width * CGFloat(articleIndex);
            ausdNewsframe.size = ausdNewsScrollView.frame.size;
            // set up text onto scrollview
            // TODO: implement array that has needed text from firebase
            let textView = UITextView(frame: ausdNewsframe);
            textView.text = "Sample text";
            self.ausdNewsScrollView.addSubview(textView);
        }
        // set size of scrollview
        ausdNewsScrollView.contentSize = CGSize(width: (ausdNewsScrollView.frame.size.width * CGFloat(ausdNewsSize)), height: ausdNewsScrollView.frame.size.height);
        ausdNewsScrollView.delegate = self;
    }
    // every time scrollview stops moving
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        // gets current page num
        var pageNum = ausdNewsScrollView.contentOffset.x / ausdNewsScrollView.frame.size.width;
        // set dot of pagecontrol to correct page
        ausdNewsPageControl.currentPage = Int(pageNum); // Typecasting float to int
        
    }

}

