//
//  File.swift
//  AHS20
//
//  Created by Richard Wei on 4/21/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit
import AudioToolbox

class articlePageViewController: UIViewController, UIScrollViewDelegate{
    /*@IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var articleText: UILabel!
    @IBOutlet weak var imageScrollView: UIScrollView!
    @IBOutlet weak var imagePageControl: UIPageControl!
    @IBOutlet weak var whiteBackground: UIImageView!*/    //@IBOutlet weak var notificationBellButton: UIButton!
   
    @IBOutlet weak var articleAuthor: UILabel!
    @IBOutlet weak var articleDate: UILabel!
    @IBOutlet weak var articleTitle: UILabel!
    @IBOutlet weak var mainScrollView: UIScrollView!
    @IBOutlet weak var articleText: UILabel!
    @IBOutlet weak var imageScrollView: UIScrollView!
    @IBOutlet weak var imagePageControl: UIPageControl!
    @IBOutlet weak var whiteBackground: UIImageView!
    
    @IBOutlet weak var bookmarkButton: CustomUIButton!
    @IBOutlet weak var bookmarkOuter: CustomUIButton!
    
    var contentWidth: CGFloat = 0.0
    var imageFrame = CGRect(x: 0, y:0, width: 0, height: 0);
    var imageSize = 1;
    var articleContent: articleData?;
    
    
    @IBAction func saveArticle(sender: CustomUIButton){
        print("Bookmark");
        
       /* if (sender.isSelected == false){
            saveCurrentArticle(articleID: sender.articleID ?? ""); // TODO: change ?? to ! instead
        }
        else{
           removeCurrentArticle(articleID: sender.articleID ?? ""); // TODO: change ?? to ! instead
        }*/
        // TODO: FIX
        sender.isSelected = !sender.isSelected;
        setBookmarkColor();
        resetUpArticles = true;
    }
    
    @IBAction func exitArticle(_ sender: UIButton){
        dismiss(animated: true);
    }
    
    func setBookmarkColor(){
        if (bookmarkButton.isSelected){
            bookmarkButton.tintColor = mainThemeColor;
        }
        else{
            bookmarkButton.tintColor = UIColor.white;
        }
    }
    
    // ------------
    // TODO: Fix issue where long text gets cut off *completed, thank u for the reminder XD -em&kim
    // TODO: Fix issue where the imagescrollview doesn't allow you to go to the third image on real devices -fixed
    // TODO: Fix issue on Storyboard where the Bookmark button gets blurry - use the system bookmark image and put a circular background behind it *completed
    // ------------
    override func viewDidLoad() {
        super.viewDidLoad();
        
        print("article page")
        print(articleContent)
        
        setBookmarkColor();
        
        bookmarkOuter.setRoundedEdge(corners: [.topLeft, .topRight, .bottomLeft, .bottomRight], radius: 12);
        
        mainScrollView.topAnchor.constraint(equalToSystemSpacingBelow: view.topAnchor, multiplier: 1).isActive = true;
        mainScrollView.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;
        
    
        articleText.text = articleContent?.articleBody;
        articleText.font = UIFont(name: articleText.font.fontName, size: CGFloat(fontSize));
        articleTitle.text = articleContent?.articleTitle;
        articleDate.text = "\(articleContent?.articleID ?? -1)"; // TODO: IMPLEMENT A FUNC TO GET INT TO STRING DATE
        articleAuthor.text = "By " + (articleContent?.articleAuthor ?? " NULL Author");
        
        
        //rounded corners (bottom corners-> [.layerMaxXMaxYCorner, .layerMinXMaxYCorner])
        whiteBackground.layer.maskedCorners = [.layerMinXMinYCorner, .layerMaxXMinYCorner]
        
        //horizontal image scroll view
       /* imageScrollView.delegate = self
        
        for imageIndex in 0..<imageSize{
            let imageToDisplay = UIImage(named: "\(imageIndex).png")
            let imageView = UIImageView(image: imageToDisplay)
            
            imageScrollView.addSubview(imageView)
            
            let xCoordinate = view.frame.midX + view.frame.width * CGFloat(imageIndex) // use UIScreen.main.bounds.width instead - view.frame.width is constant size across all devices while UIScreen is different
            
            contentWidth += view.frame.width
            
            imageView.frame = CGRect(x: xCoordinate, y: view.frame.height/2, width: 100, height: 100)
        }
        
        imageScrollView.contentSize = CGSize(width: contentWidth, height: view.frame.height)*/
    }
    override func viewDidAppear(_ animated: Bool) {
        imageSize = articleContent?.articleImages?.count as? Int ?? 0;
        imagePageControl.numberOfPages = imageSize;
        imageFrame.size = imageScrollView.frame.size;
        imageFrame.size.width = UIScreen.main.bounds.size.width;
        for imageIndex in 0..<imageSize{
            imageFrame.origin.x = (imageFrame.size.width * CGFloat(imageIndex));
            
            let imageView = UIImageView(frame: imageFrame);
            //imageView.backgroundColor = UIColor.white;
            // add image here
            imageView.imgFromURL(sURL: articleContent?.articleImages?[imageIndex] ?? "");
            
            self.imageScrollView.addSubview(imageView);
        }
        imageScrollView.contentSize = CGSize(width: (imageFrame.size.width * CGFloat(imageSize)), height: imageScrollView.frame.size.height);
        imageScrollView.delegate = self;
    }
    
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        imagePageControl.currentPage = Int(imageScrollView.contentOffset.x / imageFrame.size.width);
    }
}
