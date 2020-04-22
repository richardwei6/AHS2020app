//
//  ViewController.swift
//  AHS20
//
//  Created by Richard Wei on 3/14/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import UIKit


class homeClass: UIViewController, UIScrollViewDelegate, UITabBarControllerDelegate {

    // link UI elements to swift via outlets
	
	//@IBOutlet weak var tabBar: UITabBar!
	@IBOutlet weak var monthLabel: UILabel!
	@IBOutlet weak var featuredScrollView: UIScrollView!
	@IBOutlet weak var featuredPageControl: UIPageControl!
	@IBOutlet weak var asbNewsScrollView: UIScrollView!
	@IBOutlet weak var asbNewsPageControl: UIPageControl!
	@IBOutlet weak var sportsNewsScrollView: UIScrollView!
	@IBOutlet weak var sportsNewsPageControl: UIPageControl!
	@IBOutlet weak var districtNewsScrollView: UIScrollView!
	@IBOutlet weak var districtNewsPageControl: UIPageControl!
	
	
	// TODO: get data from server
	var featuredSize = 10;
	var featuredFrame = CGRect(x:0,y:0,width:0,height:0);
	var asbNewsSize = 3;
    var asbNewsFrame = CGRect(x:0,y:0,width:0,height:0);
	var sportsNewsSize = 2;
    var sportsNewsFrame = CGRect(x:0,y:0,width:0,height:0);
    var districtNewsSize = 5;
    var districtNewsFrame = CGRect(x:0,y:0,width:0,height:0);
	
	
	
	@objc func openArticle(){
		print("Button pressed");
		performSegue(withIdentifier: "articleSegue", sender: nil);
	}
	
	@objc func bookmarkCurrentArticle(){
		print("Bookmark Button");
	}
	
	
	func smallArticle(x: CGFloat, y: CGFloat, width: CGFloat, height: CGFloat, articleNum: Int) -> UIButton{//TODO: find out a way to separate article from top and bottom
			
		let mainArticleFrame = CGRect(x: x, y: y, width: width, height: height);
		let mainArticleView = UIButton(frame: mainArticleFrame);
		
		
		let articleTextWidth = (width/2) + 10;
		
		// article timestamp
		let timestampFrame = CGRect(x: 0, y: 0, width: 70, height: 20);
		let timestamp = UILabel(frame: timestampFrame);
		timestamp.numberOfLines = 1;
		timestamp.adjustsFontSizeToFitWidth = true;
		timestamp.minimumScaleFactor = 0.5;
		timestamp.textAlignment = .left;
		timestamp.textColor = makeColor(r: 57, g: 57, b: 57);
		timestamp.font = UIFont(name: "SFProDisplay-Regular", size: 10);
		timestamp.text = "1 hour ago";
		
		
		let articleTitleFrame = CGRect(x: 0, y: 10, width: articleTextWidth, height: height/2);
		let articleTitleLabel = UILabel(frame: articleTitleFrame);
		articleTitleLabel.numberOfLines = 2;
		articleTitleLabel.adjustsFontSizeToFitWidth = true;
		articleTitleLabel.minimumScaleFactor = 0.8;
		articleTitleLabel.textAlignment = .left;
		articleTitleLabel.font = UIFont(name: "SFProDisplay-Black",size: 20);
		articleTitleLabel.text = "Auto Adjusting Long Title";
		
		
		let articleSubtitleFrame = CGRect(x: 0, y: height/2, width: articleTextWidth, height: height/2);
		let articleSubtitleContent = UILabel(frame: articleSubtitleFrame);
		articleSubtitleContent.numberOfLines = 2;
		articleSubtitleContent.textAlignment = .left;
		articleSubtitleContent.font = UIFont(name: "SFProDisplay-Regular", size: 15);
		articleSubtitleContent.text = "This is the content inside the article. What you are seeing is a preview of such article.";
		
		
		let articleImageFrame = CGRect(x: articleTextWidth + 10, y: 10, width: width - (articleTextWidth + 20), height: height - 10);
		let articleImageView = UIImageView(frame: articleImageFrame);
		articleImageView.backgroundColor = makeColor(r: 143, g: 142, b: 142); // articleDarkGreyBackground
		articleImageView.layer.cornerRadius = 10;
		
		
		
		
		mainArticleView.addSubview(timestamp);
		mainArticleView.addSubview(articleTitleLabel);
		mainArticleView.addSubview(articleSubtitleContent);
		mainArticleView.addSubview(articleImageView);
		mainArticleView.addTarget(self, action: #selector(self.openArticle), for: .touchUpInside);
		
		return mainArticleView;
	}
    
    override func viewDidLoad() { // setup function
        super.viewDidLoad()
		
        // Do any additional setup after loading the view.
		// set month -
		let month = Calendar.current.component(.month, from: Date());
		monthLabel.text = "Month " + (month < 10 ? "0":"") + String(month);
		
		
		// article variables
		let articleHorizontalPadding = CGFloat(10);
		let articleVerticalPadding = CGFloat(5);
		
		
        //let articleGreyBackground = makeColor(r: 239, g: 247, b: 237);
		let articleDarkGreyBackground = makeColor(r: 143, g: 142, b: 142);
		// scrollview variables
		let scrollViewHorizontalConstraints = CGFloat(50);
		
		
		// Featured News ----- NOTE - article is not created by smallArticle() func
		featuredPageControl.numberOfPages = featuredSize;
		featuredFrame.size = featuredScrollView.frame.size;
		featuredFrame.size.width = UIScreen.main.bounds.size.width - scrollViewHorizontalConstraints;
        for aIndex in 0..<featuredSize{
			featuredFrame.origin.x = (featuredFrame.size.width * CGFloat(aIndex));
			
			
            // create content in scrollview
			let contentView = UIButton(frame: featuredFrame); // wrapper for article
			//contentView.backgroundColor = articleGreyBackground;
			
			
			let articleImageViewFrame = CGRect(x: articleHorizontalPadding, y: articleVerticalPadding, width: featuredFrame.size.width-(2*articleHorizontalPadding), height: featuredFrame.size.height-60-articleVerticalPadding);
			let articleImageView = UIImageView(frame:articleImageViewFrame);
			articleImageView.backgroundColor = articleDarkGreyBackground;
			articleImageView.layer.cornerRadius = 10;
			
			// time stamp
			let articleTimestampFrame = CGRect(x: articleImageViewFrame.size.width - 60, y: articleImageViewFrame.size.height - 30, width: 50, height: 20);
			let articleTimestamp = UILabel(frame: articleTimestampFrame);
			articleTimestamp.backgroundColor = makeColor(r: 216, g: 216, b: 216);
			articleTimestamp.font = UIFont(name: "SFProDisplay-Regular", size: 10);
			articleTimestamp.textAlignment = .center;
			articleTimestamp.textColor = makeColor(r: 57, g: 57, b: 57);
			articleTimestamp.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
			articleTimestamp.text = "1 hour ago"; // insert code here to get time of article
			
			
			//articleImageView.addSubview(bookmarkButton); // add bookmark button to imageview
			articleImageView.addSubview(articleTimestamp); // add timestamp to imageview
			
			
			let articleTitleFrame = CGRect(x: articleHorizontalPadding+1, y: featuredFrame.size.height - 60, width: featuredFrame.size.width-(2*articleHorizontalPadding)-2, height: 60);
			let articleTitleLabel = UILabel(frame: articleTitleFrame);
			articleTitleLabel.text = "Lorem Ipsum Long Title";
			articleTitleLabel.textAlignment = .left;
			articleTitleLabel.font = UIFont(name:"SFProText-Bold",size: 25);
			
			
			//bookmark image button - 30x30
			let bookmarkFrame = CGRect(x: (featuredFrame.size.width - 40 - articleHorizontalPadding) + (featuredFrame.size.width * CGFloat(aIndex)), y: 10+articleVerticalPadding, width: 30, height: 30);
			let bookmarkButton = UIButton(frame: bookmarkFrame);
			bookmarkButton.backgroundColor = makeColor(r: 216, g: 216, b: 216);
			bookmarkButton.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
			let bookmarkImage = UIImage(systemName: "bookmark"); // get system image
			bookmarkButton.setImage(bookmarkImage, for: .normal);
			
			
            // add contentview to scrollview
			contentView.addSubview(articleTitleLabel);
			contentView.addSubview(articleImageView);
			
			//button actions
			contentView.addTarget(self, action: #selector(self.openArticle), for: .touchUpInside);
			bookmarkButton.addTarget(self, action: #selector(self.bookmarkCurrentArticle), for: .touchUpInside);
			
            self.featuredScrollView.addSubview(contentView);
			self.featuredScrollView.addSubview(bookmarkButton);
        }
        // change horizontal size of scrollview
		featuredScrollView.contentSize = CGSize(width: (featuredFrame.size.width * CGFloat(featuredSize)), height: featuredScrollView.frame.size.height);
        featuredScrollView.delegate = self;
		
		
		// ASB News -----
		asbNewsPageControl.numberOfPages = asbNewsSize;
		asbNewsFrame.size = asbNewsScrollView.frame.size;
		asbNewsFrame.size.width = UIScreen.main.bounds.width - scrollViewHorizontalConstraints;
		  for aIndex in 0..<asbNewsSize{
			  //districtNewsFrame.origin.x = (UIScreen.main.bounds.size.width-52) * CGFloat(aIndex);
			  //districtNewsFrame.size = UIScreen.main.bounds.size;
			asbNewsFrame.origin.x = asbNewsFrame.size.width * CGFloat(aIndex);
			
			  
			// create content in scrollview
			let contentView = UIView(frame: asbNewsFrame); // wrapper for article
			
			
			let bookmarkImage = UIImage(systemName: "bookmark"); // get system image
			
			// subview bookmark button - 30x30
			let bookmarkAFrame = CGRect(x: asbNewsFrame.size.width - 45, y: 15, width: 30, height: 30);
			let bookmarkAButton = UIButton(frame: bookmarkAFrame);
			bookmarkAButton.backgroundColor = makeColor(r: 216, g: 216, b: 216);
			bookmarkAButton.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
			bookmarkAButton.setImage(bookmarkImage, for: .normal);
			// B button
			let bookmarkBFrame = CGRect(x: asbNewsFrame.size.width - 45, y: 135, width: 30, height: 30);
			let bookmarkBButton = UIButton(frame: bookmarkBFrame);
			bookmarkBButton.backgroundColor = makeColor(r: 216, g: 216, b: 216);
			bookmarkBButton.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
			bookmarkBButton.setImage(bookmarkImage, for: .normal);
			
			
			//create article with function - TODO: find out a way to separate article from top and bottom
			contentView.addSubview(smallArticle(x: 5, y: 0, width: asbNewsFrame.size.width-5, height: 120, articleNum: aIndex));
			contentView.addSubview(smallArticle(x: 5, y: 120, width: asbNewsFrame.size.width-5, height: 120, articleNum: aIndex));
			
			bookmarkAButton.addTarget(self, action: #selector(bookmarkCurrentArticle), for: .touchUpInside);
			bookmarkBButton.addTarget(self, action: #selector(bookmarkCurrentArticle), for: .touchUpInside);
			
			
			contentView.addSubview(bookmarkAButton);
			contentView.addSubview(bookmarkBButton);
			
			
			self.asbNewsScrollView.addSubview(contentView);
		  }
		  // change horizontal size of scrollview
		  asbNewsScrollView.contentSize = CGSize(width: (asbNewsFrame.size.width * CGFloat(asbNewsSize)) , height: asbNewsScrollView.frame.size.height);
		  asbNewsScrollView.delegate = self;
		
        
		
		
        // Sports News -----
		sportsNewsPageControl.numberOfPages = sportsNewsSize;
		sportsNewsFrame.size = sportsNewsScrollView.frame.size;
		sportsNewsFrame.size.width = UIScreen.main.bounds.size.width - scrollViewHorizontalConstraints;
		  for aIndex in 0..<sportsNewsSize{
			sportsNewsFrame.origin.x = sportsNewsFrame.size.width * CGFloat(aIndex);
				  
			
			
			   // create content in scrollview
			let contentView = UIView(frame: sportsNewsFrame); // wrapper for article
			let bookmarkImage = UIImage(systemName: "bookmark"); // get system image
			
			// subview bookmark button - 30x30
			let bookmarkAFrame = CGRect(x: sportsNewsFrame.size.width - 45, y: 15, width: 30, height: 30);
			let bookmarkAButton = UIButton(frame: bookmarkAFrame);
			bookmarkAButton.backgroundColor = makeColor(r: 216, g: 216, b: 216);
			bookmarkAButton.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
			bookmarkAButton.setImage(bookmarkImage, for: .normal);
			// B button
			let bookmarkBFrame = CGRect(x: sportsNewsFrame.size.width - 45, y: 135, width: 30, height: 30);
			let bookmarkBButton = UIButton(frame: bookmarkBFrame);
			bookmarkBButton.backgroundColor = makeColor(r: 216, g: 216, b: 216);
			bookmarkBButton.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
			bookmarkBButton.setImage(bookmarkImage, for: .normal);
			
			
			//create article with function - TODO: find out a way to separate article from top and bottom
			contentView.addSubview(smallArticle(x: 5, y: 0, width: sportsNewsFrame.size.width-5, height: 120, articleNum: aIndex));
			contentView.addSubview(smallArticle(x: 5, y: 120, width: sportsNewsFrame.size.width-5, height: 120, articleNum: aIndex));
			
			bookmarkAButton.addTarget(self, action: #selector(bookmarkCurrentArticle), for: .touchUpInside);
			bookmarkBButton.addTarget(self, action: #selector(bookmarkCurrentArticle), for: .touchUpInside);
			
			
			contentView.addSubview(bookmarkAButton);
			contentView.addSubview(bookmarkBButton);
			self.sportsNewsScrollView.addSubview(contentView);
		  }
		  // change horizontal size of scrollview
		sportsNewsScrollView.contentSize = CGSize(width: (sportsNewsFrame.size.width * CGFloat(sportsNewsSize)), height: sportsNewsScrollView.frame.size.height);
		  sportsNewsScrollView.delegate = self;
		
		
		// District News -----
		districtNewsPageControl.numberOfPages = districtNewsSize;
		districtNewsFrame.size = districtNewsScrollView.frame.size;
		districtNewsFrame.size.width = UIScreen.main.bounds.size.width - scrollViewHorizontalConstraints;
        for aIndex in 0..<districtNewsSize{
			districtNewsFrame.origin.x = (districtNewsFrame.size.width * CGFloat(aIndex));
			
            // create content in scrollview
			let contentView = UIView(frame: districtNewsFrame); // wrapper for article
			let bookmarkImage = UIImage(systemName: "bookmark"); // get system image
			
			// subview bookmark button - 30x30
			let bookmarkAFrame = CGRect(x: districtNewsFrame.size.width - 45, y: 15, width: 30, height: 30);
			let bookmarkAButton = UIButton(frame: bookmarkAFrame);
			bookmarkAButton.backgroundColor = makeColor(r: 216, g: 216, b: 216);
			bookmarkAButton.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
			bookmarkAButton.setImage(bookmarkImage, for: .normal);
			// B button
			let bookmarkBFrame = CGRect(x: districtNewsFrame.size.width - 45, y: 135, width: 30, height: 30);
			let bookmarkBButton = UIButton(frame: bookmarkBFrame);
			bookmarkBButton.backgroundColor = makeColor(r: 216, g: 216, b: 216);
			bookmarkBButton.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
			bookmarkBButton.setImage(bookmarkImage, for: .normal);
			
			
			//create article with function - TODO: find out a way to separate article from top and bottom
			contentView.addSubview(smallArticle(x: 5, y: 0, width: districtNewsFrame.size.width-5, height: 120, articleNum: aIndex));
			contentView.addSubview(smallArticle(x: 5, y: 120, width: districtNewsFrame.size.width-5, height: 120, articleNum: aIndex));
			
			bookmarkAButton.addTarget(self, action: #selector(bookmarkCurrentArticle), for: .touchUpInside);
			bookmarkBButton.addTarget(self, action: #selector(bookmarkCurrentArticle), for: .touchUpInside);
			
			
			contentView.addSubview(bookmarkAButton);
			contentView.addSubview(bookmarkBButton);
			
            self.districtNewsScrollView.addSubview(contentView);
        }
        // change horizontal size of scrollview
		districtNewsScrollView.contentSize = CGSize(width: (districtNewsFrame.size.width * CGFloat(districtNewsSize)), height: districtNewsScrollView.frame.size.height);
        districtNewsScrollView.delegate = self;
        
    }
	
	override func viewDidLayoutSubviews() {
		super.viewDidLayoutSubviews();
		//homeLabel.setRoundedEdge(corners: [.bottomLeft, .bottomRight], radius: 30);
	}
	
	
	func  scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
		featuredPageControl.currentPage = Int(featuredScrollView.contentOffset.x / featuredFrame.size.width);
		
		asbNewsPageControl.currentPage = Int(asbNewsScrollView.contentOffset.x / asbNewsFrame.size.width);
		
		sportsNewsPageControl.currentPage = Int(sportsNewsScrollView.contentOffset.x / sportsNewsFrame.size.width);
		
		districtNewsPageControl.currentPage = Int(districtNewsScrollView.contentOffset.x / districtNewsFrame.size.width);
	}
	

}

