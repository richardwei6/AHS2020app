//
//  ViewController.swift
//  AHS20
//
//  Created by Richard Wei on 3/14/20.
//  Copyright © 2020 AHS. All rights reserved.
//


// ----- READ: Hello whomever might be reading this. I have many custom features added to this code that you won't find on stock swift projects. This is why I have included some notes that you might want to read below:
/*
- sharedFunc.swift includes the shared functions/classes between all swift files. You can access any of theses functions from any swift file
- CustomUIButton is a custom class that I created that is an extension of the regular UIButton class. The main different to this class is that there are extra data types that allow you allow you to pass data to a ".addTarget" @objc function that you normally wouldn't be able to do. The data types can be found in sharedFunc.swift
*/
import UIKit
import AudioToolbox
import Firebase
import FirebaseDatabase

class homeClass: UIViewController, UIScrollViewDelegate, UITabBarControllerDelegate {
	
	// link UI elements to swift via outlets
	
	//@IBOutlet weak var tabBar: UITabBar!
	/*@IBOutlet weak var featuredScrollView: UIScrollView!
	@IBOutlet weak var featuredPageControl: UIPageControl!
	@IBOutlet weak var asbNewsScrollView: UIScrollView!
	@IBOutlet weak var asbNewsPageControl: UIPageControl!
	@IBOutlet weak var sportsNewsScrollView: UIScrollView!
	@IBOutlet weak var sportsNewsPageControl: UIPageControl!
	@IBOutlet weak var districtNewsScrollView: UIScrollView!
	@IBOutlet weak var districtNewsPageControl: UIPageControl!*/
	@IBOutlet weak var mainScrollView: UIScrollView!
	@IBOutlet weak var featuredScrollView: UIScrollView!
	@IBOutlet weak var featuredPageControl: UIPageControl!
	@IBOutlet weak var asbNewsScrollView: UIScrollView!
	@IBOutlet weak var asbNewsPageControl: UIPageControl!
	@IBOutlet weak var sportsNewsScrollView: UIScrollView!
	@IBOutlet weak var sportsNewsPageControl: UIPageControl!
	@IBOutlet weak var districtNewsScrollView: UIScrollView!
	@IBOutlet weak var districtNewsPageControl: UIPageControl!
	
	@IBOutlet weak var featuredLabel: UILabel!
	@IBOutlet weak var sportsLabel: UILabel!
	@IBOutlet weak var districtLabel: UILabel!
	@IBOutlet weak var asbLabel: UILabel!
	
	@IBOutlet weak var featuredMissingLabel: UILabel!
	
	let loading = "Loading...";
	
	let bookmarkImageVerticalInset = CGFloat(5);
	let bookmarkImageHorizontalInset = CGFloat(7);
	
	let bookmarkImageUI = UIImage(named: "invertedbookmark");
	//let bookmarkImageUI = UIImage(systemName: "bookmark");
	
	// TODO: get data from server
	var featuredSize = 6;
	var featuredFrame = CGRect(x:0,y:0,width:0,height:0);
	var asbNewsSize = 1;
	var asbNewsFrame = CGRect(x:0,y:0,width:0,height:0);
	var sportsNewsSize = 1;
	var sportsNewsFrame = CGRect(x:0,y:0,width:0,height:0);
	var districtNewsSize = 1;
	var districtNewsFrame = CGRect(x:0,y:0,width:0,height:0);
	
	var refreshControl = UIRefreshControl();
	var featuredArticles = [articleData]();
	
	func getHomeArticleData(){
		setUpConnection();
		if (internetConnected){
			print("ok -------- loading articles - home");
			featuredArticles = [articleData]();
			homeArticleList = [[articleData]](repeating: [articleData](), count: 3);
			
			for i in 0...2{
				var s: String; // path inside homepage
				switch i {
				case 0: // asb
					s = "asb";
					break;
				case 1: // sports
					s = "sports";
					break;
				case 2: // district
					s = "district";
					break;
				default:
					s = "";
					break;
				}
				
				//print(s);
				ref.child("homepage").child(s).observeSingleEvent(of: .value) { (snapshot) in
					//print(s);
					
					//print(snapshot.childrenCount)
					let enumerator = snapshot.children;
					var temp = [articleData](); // temporary array
					while let article = enumerator.nextObject() as? DataSnapshot{ // each article
						//print(article);
						
						
						let enumerator = article.children;
						var singleArticle = articleData();
						
						singleArticle.articleID = article.key as! String;
						
						
						while let articleContent = enumerator.nextObject() as? DataSnapshot{ // data inside article
						
							
							if (articleContent.key == "articleAuthor"){
								singleArticle.articleAuthor = articleContent.value as? String;
							}
							else if (articleContent.key == "articleBody"){
								singleArticle.articleBody = articleContent.value as? String;
							}
							else if (articleContent.key == "articleUnixEpoch"){
								singleArticle.articleUnixEpoch = articleContent.value as? Int64;
							}
							else if (articleContent.key == "articleImages"){
								
								var tempImage = [String]();
								let imageIt = articleContent.children;
								while let image = imageIt.nextObject() as? DataSnapshot{
									tempImage.append(image.value as! String);
								}
								//print(tempImage)
								singleArticle.articleImages = tempImage;
							}
							else if (articleContent.key == "articleTitle"){
								
								singleArticle.articleTitle = articleContent.value as? String;
							}
							else if (articleContent.key == "isFeatured"){
								singleArticle.isFeatured = (articleContent.value as? Int == 0 ? false : true);
							}
							
							
						}
						// print("append to main")
						// print(temp.count);
						//print(singleArticle.articleTitle);
						//print(singleArticle.articleImages);
						singleArticle.articleCatagory = s.prefix(1).capitalized + s.dropFirst();
						temp.append(singleArticle);
						//print(singleArticle.isFeatured);
						if (singleArticle.isFeatured == true){
							self.featuredArticles.append(singleArticle);
						}
					}
					homeArticleList[i] = temp;
					self.setUpAllViews();
					self.refreshControl.endRefreshing();
				};
			}
		}
		else{
			//setUpAllViews();
			print("no network detected - home");
			featuredLabel.text = "NO Connection";
			asbLabel.text = "NO Connection";
			sportsLabel.text = "NO Connection";
			districtLabel.text = "NO Connection";
			let infoPopup = UIAlertController(title: "No internet connection detected", message: "No articles were loaded", preferredStyle: UIAlertController.Style.alert);
			infoPopup.addAction(UIAlertAction(title: "Ok", style: .default, handler: { (action: UIAlertAction!) in
				self.refreshControl.endRefreshing();
			}));
			present(infoPopup, animated: true, completion: nil);
		}
	}
	
	func setUpColorOfBookmark(sender: inout CustomUIButton){
		if (savedArticleClass.isSavedCurrentArticle(articleID: sender.articleCompleteData.articleID!)){ // TODO: implement sender.articleID
			sender.tintColor = mainThemeColor;
		}
		else{
			sender.tintColor = UIColor.white;
		}
	}
	
	
	@objc func openArticle(sender: CustomUIButton){
		print("Button pressed");
		//performSegue(withIdentifier: "articleSegue", sender: nil);
	//	print("home page notif");
	//	print(sender.articleCompleteData)
		let articleDataDict: [String: articleData] = ["articleContent" : sender.articleCompleteData];
		NotificationCenter.default.post(name: NSNotification.Name(rawValue: "article"), object: nil, userInfo: articleDataDict);
	}

	
	@objc func bookmarkCurrentArticle(sender: CustomUIButton){
		//print("bookmark - \(savedArticleClass.isSavedCurrentArticle(articleID: sender.articleCompleteData.articleID!))")
		//print("Bookmark Button - \(sender.articleCompleteData)");
		if (savedArticleClass.isSavedCurrentArticle(articleID: sender.articleCompleteData.articleID!) == false){
			sender.tintColor = mainThemeColor;
			savedArticleClass.saveCurrArticle(articleID: sender.articleCompleteData.articleID!, article: sender.articleCompleteData);
		}
		else{
			sender.tintColor = UIColor.white;
			savedArticleClass.removeCurrArticle(articleID: sender.articleCompleteData.articleID!);
		}
		if (sender.articleCompleteData.isFeatured){
			setUpAllViews();
		}
	}
	
	
	func smallArticle(x: CGFloat, y: CGFloat, width: CGFloat, height: CGFloat, articleSingle: articleData) -> CustomUIButton{//TODO: find out a way to separate article from top and bottom
		
		let mainArticleFrame = CGRect(x: x, y: y, width: width, height: height);
		let mainArticleView = CustomUIButton(frame: mainArticleFrame);
		
		
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
		timestamp.text = epochClass.epochToString(epoch: articleSingle.articleUnixEpoch ?? -1);
		
		
		let articleTitleFrame = CGRect(x: 0, y: 10, width: articleTextWidth, height: height/2);
		let articleTitleLabel = UILabel(frame: articleTitleFrame);
		articleTitleLabel.numberOfLines = 2;
		articleTitleLabel.adjustsFontSizeToFitWidth = true;
		articleTitleLabel.minimumScaleFactor = 0.8;
		articleTitleLabel.textAlignment = .left;
		articleTitleLabel.font = UIFont(name: "SFProText-Bold",size: 20);
		articleTitleLabel.text =  articleSingle.articleTitle; // DATA
		
		
		let articleSubtitleFrame = CGRect(x: 0, y: height/2, width: articleTextWidth, height: height/2);
		let articleSubtitleContent = UILabel(frame: articleSubtitleFrame);
		articleSubtitleContent.numberOfLines = 2;
		articleSubtitleContent.textAlignment = .left;
		articleSubtitleContent.font = UIFont(name: "SFProDisplay-Regular", size: 15);
		articleSubtitleContent.text = articleSingle.articleBody; // DATA
		
		let articleImageFrame = CGRect(x: articleTextWidth + 10, y: 10, width: width - (articleTextWidth + 18), height: height - 10);
		let articleImageView = UIImageView(frame: articleImageFrame);
		articleImageView.backgroundColor = makeColor(r: 143, g: 142, b: 142); // articleDarkGreyBackground
		if (articleSingle.articleImages?.count ?? 0 > 0){
			articleImageView.imgFromURL(sURL: articleSingle.articleImages?[0] ?? "");
			articleImageView.contentMode = .scaleAspectFill;
		}
		
		//articleImageView.layer.cornerRadius = 10;
		articleImageView.setRoundedEdge(corners: [.topLeft, .topRight, .bottomLeft, .bottomRight], radius: 10);
		
		mainArticleView.addSubview(timestamp);
		mainArticleView.addSubview(articleTitleLabel);
		mainArticleView.addSubview(articleSubtitleContent);
		mainArticleView.addSubview(articleImageView);
		mainArticleView.addTarget(self, action: #selector(self.openArticle), for: .touchUpInside);
		
		mainArticleView.articleCompleteData = articleSingle;
		
		return mainArticleView;
	}
	
	func arrayToPairs(a: [articleData]) -> [[articleData]]{
		var ans = [[articleData]]();
		var temp = [articleData](); // pairs
		for i in a{
			temp.append(i);
			if (temp.count == 2){
				ans.append(temp);
				temp = [articleData]();
			}
		}
		if (temp.count > 0){
			ans.append(temp);
		}
		return ans;
	}
	
	func setUpAllViews(){
		
		//	print("home");
		//	print(homeArticleList.count);
		setUpConnection();
		if (internetConnected && homeArticleList[0].count > 0 && homeArticleList[1].count > 0 && homeArticleList[2].count > 0){
			
			featuredLabel.text = "FEATURED";
			asbLabel.text = "ASB NEWS";
			sportsLabel.text = "SPORTS NEWS";
			districtLabel.text = "DISTRICT NEWS";
			
			//print("home");
			//print(homeArticleList)
			let asbArticlePairs = arrayToPairs(a: homeArticleList[0]);
			let sportsArticlePairs = arrayToPairs(a: homeArticleList[1]);
			let districtArticlePairs = arrayToPairs(a: homeArticleList[2]);
			featuredSize = featuredArticles.count;
			asbNewsSize = asbArticlePairs.count;
			sportsNewsSize = sportsArticlePairs.count;
			districtNewsSize = districtArticlePairs.count;
			/*print(asbArticlePairs);
			print(sportsArticlePairs);
			print(districtArticlePairs);
			print("home")
			print(featuredSize);
			print(asbNewsSize);
			print(sportsNewsSize);
			print(districtNewsSize);*/
			
			let bookMarkBackground = makeColor(r: 165, g: 165, b: 165);
			
			
			let articleDarkGreyBackground = makeColor(r: 143, g: 142, b: 142);
			// scrollview variables
			let scrollViewHorizontalConstraints = CGFloat(36);
			
			for view in featuredScrollView.subviews{
				view.removeFromSuperview();
			}
			for view in asbNewsScrollView.subviews{
				view.removeFromSuperview();
			}
			for view in sportsNewsScrollView.subviews{
				view.removeFromSuperview();
			}
			for view in districtNewsScrollView.subviews{
				view.removeFromSuperview();
			}
			
			if (featuredSize > 0){
				// Featured News ----- NOTE - article is not created by smallArticle() func
				featuredMissingLabel.isHidden = true;
				featuredScrollView.isHidden = false;
				featuredPageControl.isHidden = false;
				featuredPageControl.numberOfPages = featuredSize;
				featuredFrame.size = featuredScrollView.frame.size;
				featuredFrame.size.width = UIScreen.main.bounds.size.width;
				for aIndex in 0..<featuredSize{
					featuredFrame.origin.x = (featuredFrame.size.width * CGFloat(aIndex));
					
					let currArticle = featuredArticles[aIndex];
					
					let outerContentView = CustomUIButton(frame: featuredFrame);
					let articleTimeStampLength = CGFloat(72)
					//outerContentView.backgroundColor = UIColor.gray;
					
					
					let innerContentViewContraint = CGFloat(24);
					let contentViewFrame = CGRect(x: innerContentViewContraint, y: 0, width: featuredFrame.size.width - (2*innerContentViewContraint), height: featuredFrame.size.height);
					let contentView = CustomUIButton(frame: contentViewFrame);
					
					let articleImageViewFrame = CGRect(x: 0, y: 0, width: contentViewFrame.size.width, height: contentViewFrame.size.height - 40); // add to contentView
					let articleImageView = UIImageView(frame:articleImageViewFrame);
					articleImageView.backgroundColor = articleDarkGreyBackground;
					
					if (currArticle.articleImages?.count ?? 0 > 0){
						articleImageView.imgFromURL(sURL: currArticle.articleImages?[0] ?? "");
						articleImageView.setRoundedEdge(corners: [.topLeft, .topRight, .bottomLeft, .bottomRight], radius: 10);
						articleImageView.contentMode = .scaleAspectFill;
					}
					
					
					// time stamp
					let articleTimestampFrame = CGRect(x: articleImageViewFrame.size.width - (10+articleTimeStampLength), y: articleImageViewFrame.size.height - 30, width: articleTimeStampLength, height: 20);
					let articleTimestamp = UILabel(frame: articleTimestampFrame);
					articleTimestamp.backgroundColor = makeColor(r: 197, g: 197, b: 197);
					articleTimestamp.font = UIFont(name: "SFProDisplay-Regular", size: 10);
					articleTimestamp.textAlignment = .center;
					articleTimestamp.textColor = makeColor(r: 57, g: 57, b: 57);
					articleTimestamp.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
					articleTimestamp.text = epochClass.epochToString(epoch: currArticle.articleUnixEpoch ?? -1); // insert code here to get time of article
					
					
					articleImageView.addSubview(articleTimestamp); // add timestamp to imageview
					
					
					let articleTitleFrame = CGRect(x: 0, y: contentViewFrame.size.height - 30, width: contentViewFrame.size.width, height: 30);
					let articleTitleLabel = UILabel(frame: articleTitleFrame);
					articleTitleLabel.text = currArticle.articleTitle; // DATA
					articleTitleLabel.textAlignment = .left;
					articleTitleLabel.font = UIFont(name:"SFProText-Bold",size: 25);
					
					contentView.addSubview(articleImageView);
					contentView.addSubview(articleTitleLabel);
					
					
					let bookmarkFrame = CGRect(x: (featuredFrame.size.width - 40 - innerContentViewContraint) + (featuredFrame.size.width * CGFloat(aIndex)), y: 10, width: 30, height: 30);
					var bookmarkButton = CustomUIButton(frame: bookmarkFrame);
					bookmarkButton.backgroundColor = bookMarkBackground;
					bookmarkButton.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
					let bookmarkImage = bookmarkImageUI; // get system image
					bookmarkButton.setImage(bookmarkImage, for: .normal);
					//bookmarkButton.tintColor = bookMarkTint;
					bookmarkButton.articleCompleteData = currArticle;
					setUpColorOfBookmark(sender: &bookmarkButton);
					bookmarkButton.isSelected = false;
					bookmarkButton.imageEdgeInsets = UIEdgeInsets(top: bookmarkImageVerticalInset, left: bookmarkImageHorizontalInset, bottom: bookmarkImageVerticalInset, right: bookmarkImageHorizontalInset);
					
					
					outerContentView.articleCompleteData = currArticle;
					contentView.articleCompleteData = currArticle;
					
					contentView.addTarget(self, action: #selector(openArticle), for: .touchUpInside);
					
					
					outerContentView.addSubview(contentView);
					
					outerContentView.addTarget(self, action: #selector(openArticle), for: .touchUpInside);
					bookmarkButton.addTarget(self, action: #selector(bookmarkCurrentArticle), for: .touchUpInside);
					//articleImageView.layer.cornerRadius = 10;
					
					
					self.featuredScrollView.addSubview(outerContentView);
					self.featuredScrollView.addSubview(bookmarkButton);
				}
				// change horizontal size of scrollview
				featuredScrollView.contentSize = CGSize(width: (featuredFrame.size.width * CGFloat(featuredSize)), height: featuredScrollView.frame.size.height);
				featuredScrollView.delegate = self;
			}
			else{
				featuredMissingLabel.isHidden = false;
				featuredScrollView.isHidden = true;
				featuredPageControl.isHidden = true;
			}
			
			// ASB News -----
			asbNewsPageControl.numberOfPages = asbNewsSize;
			asbNewsFrame.size = asbNewsScrollView.frame.size;
			asbNewsFrame.size.width = UIScreen.main.bounds.width - scrollViewHorizontalConstraints;
			for aIndex in 0..<asbNewsSize{
				//districtNewsFrame.origin.x = (UIScreen.main.bounds.size.width-52) * CGFloat(aIndex);
				//districtNewsFrame.size = UIScreen.main.bounds.size;
				asbNewsFrame.origin.x = 1+(asbNewsFrame.size.width * CGFloat(aIndex));
				
				
				// create content in scrollview
				let contentView = UIView(frame: asbNewsFrame); // wrapper for article
				
				
				let bookmarkImage = bookmarkImageUI; // get system image
				
				// subview bookmark button - 30x30
				let bookmarkAFrame = CGRect(x: asbNewsFrame.size.width - 45, y: 15, width: 30, height: 30);
				var bookmarkAButton = CustomUIButton(frame: bookmarkAFrame);
				bookmarkAButton.backgroundColor = bookMarkBackground;
				bookmarkAButton.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
				bookmarkAButton.setImage(bookmarkImage, for: .normal);
				//bookmarkAButton.tintColor = bookMarkTint;
				bookmarkAButton.articleCompleteData = asbArticlePairs[aIndex][0];
				setUpColorOfBookmark(sender: &bookmarkAButton);
				bookmarkAButton.isSelected = false;
				bookmarkAButton.imageEdgeInsets = UIEdgeInsets(top: bookmarkImageVerticalInset, left: bookmarkImageHorizontalInset, bottom: bookmarkImageVerticalInset, right: bookmarkImageHorizontalInset);
				contentView.addSubview(smallArticle(x: 5, y: 0, width: asbNewsFrame.size.width-5, height: 120, articleSingle: asbArticlePairs[aIndex][0]));
				bookmarkAButton.addTarget(self, action: #selector(bookmarkCurrentArticle), for: .touchUpInside);
				contentView.addSubview(bookmarkAButton);
				
				if (asbArticlePairs[aIndex].count == 2){
					// B button
					let bookmarkBFrame = CGRect(x: asbNewsFrame.size.width - 45, y: 135, width: 30, height: 30);
					var bookmarkBButton = CustomUIButton(frame: bookmarkBFrame);
					bookmarkBButton.backgroundColor = bookMarkBackground;
					bookmarkBButton.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
					bookmarkBButton.setImage(bookmarkImage, for: .normal);
					//bookmarkBButton.tintColor = bookMarkTint;
					bookmarkBButton.articleCompleteData = asbArticlePairs[aIndex][1];
					setUpColorOfBookmark(sender: &bookmarkBButton);
					bookmarkBButton.isSelected = false;
					bookmarkBButton.imageEdgeInsets = UIEdgeInsets(top: bookmarkImageVerticalInset, left: bookmarkImageHorizontalInset, bottom: bookmarkImageVerticalInset, right: bookmarkImageHorizontalInset);
					contentView.addSubview(smallArticle(x: 5, y: 120, width: asbNewsFrame.size.width-5, height: 120, articleSingle: asbArticlePairs[aIndex][1]));
					bookmarkBButton.addTarget(self, action: #selector(bookmarkCurrentArticle), for: .touchUpInside);
					contentView.addSubview(bookmarkBButton);
				}
				
				self.asbNewsScrollView.addSubview(contentView);
			}
			// change horizontal size of scrollview
			asbNewsScrollView.contentSize = CGSize(width: 1+(asbNewsFrame.size.width * CGFloat(asbNewsSize)) , height: asbNewsScrollView.frame.size.height);
			asbNewsScrollView.delegate = self;
			
			
			
			
			// Sports News -----
			sportsNewsPageControl.numberOfPages = sportsNewsSize;
			sportsNewsFrame.size = sportsNewsScrollView.frame.size;
			sportsNewsFrame.size.width = UIScreen.main.bounds.size.width - scrollViewHorizontalConstraints;
			for aIndex in 0..<sportsNewsSize{
				sportsNewsFrame.origin.x = 1+(sportsNewsFrame.size.width * CGFloat(aIndex));
				
				
				
				// create content in scrollview
				let contentView = UIView(frame: sportsNewsFrame); // wrapper for article
				let bookmarkImage = bookmarkImageUI; // get system image
				
				// subview bookmark button - 30x30
				let bookmarkAFrame = CGRect(x: sportsNewsFrame.size.width - 45, y: 15, width: 30, height: 30);
				var bookmarkAButton = CustomUIButton(frame: bookmarkAFrame);
				bookmarkAButton.backgroundColor = bookMarkBackground;
				bookmarkAButton.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
				bookmarkAButton.setImage(bookmarkImage, for: .normal);
				//bookmarkAButton.tintColor = bookMarkTint;
				bookmarkAButton.articleCompleteData = sportsArticlePairs[aIndex][0];
				setUpColorOfBookmark(sender: &bookmarkAButton);
				bookmarkAButton.isSelected = false;
				bookmarkAButton.imageEdgeInsets = UIEdgeInsets(top: bookmarkImageVerticalInset, left: bookmarkImageHorizontalInset, bottom: bookmarkImageVerticalInset, right: bookmarkImageHorizontalInset);
				contentView.addSubview(smallArticle(x: 5, y: 0, width: sportsNewsFrame.size.width-5, height: 120, articleSingle: sportsArticlePairs[aIndex][0]));
				bookmarkAButton.addTarget(self, action: #selector(bookmarkCurrentArticle), for: .touchUpInside);
				contentView.addSubview(bookmarkAButton);
				
				if (sportsArticlePairs[aIndex].count == 2){
					// B button
					let bookmarkBFrame = CGRect(x: sportsNewsFrame.size.width - 45, y: 135, width: 30, height: 30);
					var bookmarkBButton = CustomUIButton(frame: bookmarkBFrame);
					bookmarkBButton.backgroundColor = bookMarkBackground;
					bookmarkBButton.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
					bookmarkBButton.setImage(bookmarkImage, for: .normal);
					//bookmarkBButton.tintColor = bookMarkTint;
					bookmarkBButton.articleCompleteData = sportsArticlePairs[aIndex][1];
					setUpColorOfBookmark(sender: &bookmarkBButton);
					bookmarkBButton.isSelected = false;
					bookmarkBButton.imageEdgeInsets = UIEdgeInsets(top: bookmarkImageVerticalInset, left: bookmarkImageHorizontalInset, bottom: bookmarkImageVerticalInset, right: bookmarkImageHorizontalInset);
					contentView.addSubview(smallArticle(x: 5, y: 120, width: sportsNewsFrame.size.width-5, height: 120, articleSingle: sportsArticlePairs[aIndex][1]));
					bookmarkBButton.addTarget(self, action: #selector(bookmarkCurrentArticle), for: .touchUpInside);
					contentView.addSubview(bookmarkBButton);
				}
				
				self.sportsNewsScrollView.addSubview(contentView);
			}
			// change horizontal size of scrollview
			sportsNewsScrollView.contentSize = CGSize(width: 1+(sportsNewsFrame.size.width * CGFloat(sportsNewsSize)), height: sportsNewsScrollView.frame.size.height);
			sportsNewsScrollView.delegate = self;
			
			
			// District News -----
			districtNewsPageControl.numberOfPages = districtNewsSize;
			districtNewsFrame.size = districtNewsScrollView.frame.size;
			districtNewsFrame.size.width = UIScreen.main.bounds.size.width - scrollViewHorizontalConstraints;
			for aIndex in 0..<districtNewsSize{
				districtNewsFrame.origin.x = 1+(districtNewsFrame.size.width * CGFloat(aIndex));
				
				// create content in scrollview
				let contentView = UIView(frame: districtNewsFrame); // wrapper for article
				let bookmarkImage = bookmarkImageUI; // get system image
				
				// subview bookmark button - 30x30
				let bookmarkAFrame = CGRect(x: districtNewsFrame.size.width - 45, y: 15, width: 30, height: 30);
				var bookmarkAButton = CustomUIButton(frame: bookmarkAFrame);
				bookmarkAButton.backgroundColor = bookMarkBackground;
				bookmarkAButton.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
				bookmarkAButton.setImage(bookmarkImage, for: .normal);
				//bookmarkAButton.tintColor = bookMarkTint;
				bookmarkAButton.articleCompleteData = districtArticlePairs[aIndex][0];
				setUpColorOfBookmark(sender: &bookmarkAButton);
				bookmarkAButton.isSelected = false;
				bookmarkAButton.imageEdgeInsets = UIEdgeInsets(top: bookmarkImageVerticalInset, left: bookmarkImageHorizontalInset, bottom: bookmarkImageVerticalInset, right: bookmarkImageHorizontalInset);
				contentView.addSubview(smallArticle(x: 5, y: 0, width: districtNewsFrame.size.width-5, height: 120, articleSingle: districtArticlePairs[aIndex][0]));
				bookmarkAButton.addTarget(self, action: #selector(bookmarkCurrentArticle), for: .touchUpInside);
				contentView.addSubview(bookmarkAButton);
				
				if (districtArticlePairs[aIndex].count == 2){
					// B button
					let bookmarkBFrame = CGRect(x: districtNewsFrame.size.width - 45, y: 135, width: 30, height: 30);
					var bookmarkBButton = CustomUIButton(frame: bookmarkBFrame);
					bookmarkBButton.backgroundColor = bookMarkBackground;
					bookmarkBButton.setRoundedEdge(corners: [.topRight,.topLeft,.bottomLeft,.bottomRight], radius: 6);
					bookmarkBButton.setImage(bookmarkImage, for: .normal);
					//bookmarkBButton.tintColor = bookMarkTint;
					bookmarkBButton.articleCompleteData = districtArticlePairs[aIndex][1];
					setUpColorOfBookmark(sender: &bookmarkBButton);
					bookmarkBButton.isSelected = false;
					bookmarkBButton.imageEdgeInsets = UIEdgeInsets(top: bookmarkImageVerticalInset, left: bookmarkImageHorizontalInset, bottom: bookmarkImageVerticalInset, right: bookmarkImageHorizontalInset);
					contentView.addSubview(smallArticle(x: 5, y: 120, width: districtNewsFrame.size.width-5, height: 120, articleSingle: districtArticlePairs[aIndex][1]));
					bookmarkBButton.addTarget(self, action: #selector(bookmarkCurrentArticle), for: .touchUpInside);
					contentView.addSubview(bookmarkBButton);
				}
				
				self.districtNewsScrollView.addSubview(contentView);
			}
			// change horizontal size of scrollview
			districtNewsScrollView.contentSize = CGSize(width: 1+(districtNewsFrame.size.width * CGFloat(districtNewsSize)), height: districtNewsScrollView.frame.size.height);
			districtNewsScrollView.delegate = self;
		}
	}
	
	@objc func refreshAllArticles(){
		print("refresh");
		//article.setUpLocalData();
		featuredLabel.text = loading;
		asbLabel.text = loading;
		sportsLabel.text = loading;
		districtLabel.text = loading;
		getHomeArticleData();
	}
	
	override func viewDidLoad() { // setup function
		super.viewDidLoad();
		
		
		featuredLabel.text = loading;
		asbLabel.text = loading;
		sportsLabel.text = loading;
		districtLabel.text = loading;
		
	  	getHomeArticleData();
		refreshControl.setValue(5, forKey: "_snappingHeight")
		refreshControl.addTarget(self, action: #selector(refreshAllArticles), for: UIControl.Event.valueChanged);
		mainScrollView.addSubview(refreshControl);
	}
	
	override func viewDidAppear(_ animated: Bool) {
		if (resetUpArticles){ // refresh bookmarks
			setUpAllViews();
			resetUpArticles = false;
		}
	}
	
	
	func  scrollViewDidScroll(_ scrollView: UIScrollView) {
		if (scrollView.tag != -1){
			featuredPageControl.currentPage = Int(featuredScrollView.contentOffset.x / featuredFrame.size.width);
			
			asbNewsPageControl.currentPage = Int(asbNewsScrollView.contentOffset.x / asbNewsFrame.size.width);
			
			sportsNewsPageControl.currentPage = Int(sportsNewsScrollView.contentOffset.x / sportsNewsFrame.size.width);
			
			districtNewsPageControl.currentPage = Int(districtNewsScrollView.contentOffset.x / districtNewsFrame.size.width);
		}
	}
	
	
}

