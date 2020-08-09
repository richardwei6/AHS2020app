//
//  bulletinPage.swift
//  AHS20
//
//  Created by Richard Wei on 4/5/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit
import AudioToolbox
import Firebase
import FirebaseDatabase



class bulletinClass: UIViewController, UIScrollViewDelegate, UITabBarControllerDelegate {
    
    @IBOutlet weak var filterScrollView: UIScrollView!
    @IBOutlet weak var bulletinScrollView: UIScrollView!
    
    
    @IBOutlet weak var filterScrollViewHeightContraint: NSLayoutConstraint!
    
    let refreshControl = UIRefreshControl();
    
    // padding variables
    let articleHorizontalPadding = CGFloat(12);
    let articleVerticalPadding = CGFloat(12);
    let articleVerticalSize = CGFloat(130);
    
    
    let filterSize = 5;
    var filterFrame = CGRect(x:0,y:0,width: 0, height: 0);
    
    let filterName = ["Academics", "Athletics", "Clubs", "Colleges", "Reference"];
    
    var selectedFilters: [Bool] = [false, false, false, false, false]; // selected types in this order - seniors, colleges, events, athletics, reference, and others
    
    var currentArticles = [[bulletinArticleData]]();
    
    // icon stuff
    var iconViewFrame: CGRect?;
    var filterScrollViewMaxHeight: CGFloat?;
    var filterScrollViewMinHeight: CGFloat?;
    
    func getBulletinArticleData() {
        setUpConnection();
        if (internetConnected){
            //print("ok -------- loading articles - bulletin");
            bulletinArticleList = [[bulletinArticleData]](repeating: [bulletinArticleData](), count: 5);
            for i in 0...4{
                var s: String; // path inside homepage
                switch i {
                case 0: // seniors
                    s = "Academics";
                    break;
                case 1: // colleges
                    s = "Athletics";
                    break;
                case 2: // events
                    s = "Clubs";
                    break;
                case 3: // athletics
                    s = "Colleges";
                    break;
                case 4: // reference
                    s = "Reference";
                    break;
                default:
                    s = "";
                    break;
                }
                ref.child("bulletin").child(s).observeSingleEvent(of: .value) { (snapshot) in
                    let enumerator = snapshot.children;
                    var temp = [bulletinArticleData](); // temporary array
                    while let article = enumerator.nextObject() as? DataSnapshot{ // each article
                        let enumerator = article.children;
                        var singleArticle = bulletinArticleData();
                        
                        singleArticle.articleID = article.key;
                        
                        while let articleContent = enumerator.nextObject() as? DataSnapshot{ // data inside article
                            if (articleContent.key == "articleBody"){
                                singleArticle.articleBody = articleContent.value as? String;
                            }
                            else if (articleContent.key == "articleUnixEpoch"){
                                singleArticle.articleUnixEpoch = articleContent.value as? Int64;
                            }
                                
                            else if (articleContent.key == "articleTitle"){
                                singleArticle.articleTitle = articleContent.value as? String;
                            }
                            else if (articleContent.key == "hasHTML"){
                                singleArticle.hasHTML = (articleContent.value as? Int == 0 ? false : true);
                            }
                        }
                        singleArticle.articleCatagory = s;
                        singleArticle.articleType = i;
                        temp.append(singleArticle);
                    }
                    bulletinArticleList[i] = temp;
                    self.refreshControl.endRefreshing();
                    self.generateBulletin();
                };
            }
        }
        else{
            let infoPopup = UIAlertController(title: "No internet connection detected", message: "No articles were loaded", preferredStyle: UIAlertController.Style.alert);
            infoPopup.addAction(UIAlertAction(title: "Ok", style: .default, handler: { (action: UIAlertAction!) in
                self.refreshControl.endRefreshing();
            }));
            present(infoPopup, animated: true, completion: nil);
        }
        
    }
    
    @objc func openArticle(sender: CustomUIButton){
        if (bulletinReadDict[sender.articleCompleteData.articleID ?? ""] == nil){
            bulletinReadDict[sender.articleCompleteData.articleID ?? ""] = true;
            saveBullPref();
            generateBulletin();
        }
        let articleDataDict: [String: articleData] = ["articleContent" : sender.articleCompleteData];
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: "article"), object: nil, userInfo: articleDataDict);
    }
    
    @objc func addFilter(sender: CustomUIButton){
        sender.isSelected = !sender.isSelected;
        selectedFilters[sender.articleIndex] = sender.isSelected;
        setUpFilters();
        generateBulletin();
        UIImpactFeedbackGenerator(style: .light).impactOccurred();
    }
    
    
    func filterArticles() -> [[bulletinArticleData]]{
        var copy = [bulletinArticleData]();
        for i in 0..<totalArticles.count{
            if (selectedFilters[totalArticles[i].articleType] == true){
                copy.append(totalArticles[i]);
            }
        }
        return copy.count == 0 ? filterRead(copy: totalArticles) : filterRead(copy: copy);
    }
    
    func sortArticlesByTime(a: bulletinArticleData, b: bulletinArticleData)->Bool{
        let currTime = Int64(NSDate().timeIntervalSince1970);
        if (a.articleUnixEpoch ?? INT64_MAX > currTime && b.articleUnixEpoch ?? INT64_MAX > currTime){
            return (a.articleUnixEpoch ?? INT64_MAX) < (b.articleUnixEpoch ?? INT64_MAX);
        }
        else{
            return (a.articleUnixEpoch ?? INT64_MAX) > (b.articleUnixEpoch ?? INT64_MAX);
        }
    }
    
    func filterRead(copy: [bulletinArticleData]) -> [[bulletinArticleData]]{ // 0th row is unread 1st is read
        var output = [[bulletinArticleData]](repeating: [bulletinArticleData](), count: 2);
        loadBullPref();
        for i in copy{
            if (bulletinReadDict[i.articleID ?? ""] == true){
                output[1].append(i);
            }
            else{
                output[0].append(i);
            }
        }
        for subview in bulletinScrollView.subviews{
            if (subview != refreshControl){
                subview.removeFromSuperview();
            }
        }
        return output;
    }
    
    
    func generateBulletin(){
        if (bulletinArticleList[0].count > 0 || bulletinArticleList[1].count > 0 || bulletinArticleList[2].count > 0 || bulletinArticleList[3].count > 0 || bulletinArticleList[4].count > 0){
            totalArticles = [bulletinArticleData]();
            for i in 0...4{
                for c in bulletinArticleList[i]{
                    totalArticles.append(c);
                }
            }
            totalArticles.sort(by: sortArticlesByTime);
            currentArticles = filterArticles();
            var bulletinFrame = CGRect(x:0, y:0, width: 0, height: 0);
            
            bulletinFrame.size.height = articleVerticalSize;
            bulletinFrame.size.width = UIScreen.main.bounds.size.width - (2*articleHorizontalPadding);
            
            let catagoryFrameWidth = CGFloat(70);
            var currY = articleVerticalPadding;
            
            for article in currentArticles[0]{ // UNREAD
                bulletinFrame.origin.x = articleHorizontalPadding;
                bulletinFrame.origin.y = currY;
                currY += bulletinFrame.size.height + articleVerticalPadding;
                let articleButton = UIView(frame: bulletinFrame);
                articleButton.backgroundColor = UIColor.white;
                
                // content inside button
                let mainViewFrame = CGRect(x: 10, y: 10, width: bulletinFrame.size.width - (2*articleHorizontalPadding), height: bulletinFrame.size.height - 10);
                let mainView = CustomUIButton(frame: mainViewFrame);
                
                let articleTitleFrame = CGRect(x: 0, y : 17, width: UIScreen.main.bounds.size.width - articleHorizontalPadding - 55, height: 34);
                let articleTitleText = UILabel(frame: articleTitleFrame);
                articleTitleText.text = article.articleTitle; // insert title text here ------ temporary
                articleTitleText.font =  UIFont(name: "SFProText-Bold",size: 16);
                articleTitleText.numberOfLines = 1;
                
                let articleBodyFrame = CGRect(x: 0, y: 44, width: mainViewFrame.size.width, height: mainViewFrame.size.height - 50);
                let articleBodyText = UILabel(frame: articleBodyFrame);
                if (article.hasHTML == true){
                    articleBodyText.text = parseHTML(s: article.articleBody ?? "").string;
                }
                else{
                    articleBodyText.text = (article.articleBody ?? "");
                }
                articleBodyText.numberOfLines = 4;
                articleBodyText.font = UIFont(name: "SFProDisplay-Regular", size: 15);
                
                mainView.addSubview(articleTitleText);
                mainView.addSubview(articleBodyText);
                
                let dateTextFrame = CGRect(x: bulletinFrame.size.width - (2*articleHorizontalPadding) - 95, y : 5, width: 100, height: 25);
                let dateText = UILabel(frame: dateTextFrame);
                dateText.text = epochClass.epochToString(epoch: article.articleUnixEpoch ?? -1); // insert date here -------- temporary
                dateText.textColor = makeColor(r: 156, g: 0, b: 0);
                dateText.textAlignment = .right;
                dateText.font = UIFont(name: "SFProDisplay-Regular", size: 12);
                
                let catagoryFrame = CGRect(x: 8, y: 12, width: catagoryFrameWidth, height: 20);
                let catagory = UILabel(frame: catagoryFrame);
                catagory.text = article.articleCatagory ?? "No Cata.";
                catagory.backgroundColor = mainThemeColor;
                catagory.textAlignment = .center;
                catagory.textColor = UIColor.white;
                catagory.setRoundedEdge(corners: [.bottomRight, .bottomLeft, .topRight, .topLeft], radius: 5);
                catagory.font = UIFont(name: "SFProDisplay-Semibold", size: 12);
                
                let readLabelFrame = CGRect(x: 15 + catagoryFrame.size.width, y: 12, width: 40, height: 20);
                let readLabel = UILabel(frame: readLabelFrame);
                readLabel.text = "New";
                readLabel.backgroundColor = UIColor.systemYellow;
                readLabel.textAlignment = .center;
                readLabel.textColor = UIColor.white;
                readLabel.font = UIFont(name: "SFProDisplay-Semibold", size: 12);
                readLabel.setRoundedEdge(corners: [.bottomRight, .bottomLeft, .topRight, .topLeft], radius: 5);
                
                articleButton.addSubview(dateText);
                articleButton.addSubview(mainView);
                articleButton.addSubview(catagory);
                articleButton.addSubview(readLabel);
                
                articleButton.layer.shadowColor = UIColor.black.cgColor;
                articleButton.layer.shadowOpacity = 0.2;
                articleButton.layer.shadowRadius = 5;
                articleButton.layer.shadowOffset = CGSize(width: 0 , height:3);
                
                mainView.articleCompleteData = bulletinDataToarticleData(data: article);
                
                mainView.addTarget(self, action: #selector(self.openArticle), for: .touchUpInside);
                self.bulletinScrollView.addSubview(articleButton);
            }
            for article in currentArticles[1]{
                bulletinFrame.origin.x = articleHorizontalPadding;
                bulletinFrame.origin.y = currY;
                currY += bulletinFrame.size.height + articleVerticalPadding;
                let articleButton = UIView(frame: bulletinFrame);
                articleButton.backgroundColor =  makeColor(r: 250, g: 250, b: 250);
                
                
                // content inside button
                let mainViewFrame = CGRect(x: 10, y: 10, width: bulletinFrame.size.width - (2*articleHorizontalPadding), height: bulletinFrame.size.height - 10);
                let mainView = CustomUIButton(frame: mainViewFrame);
                
                
                let articleTitleFrame = CGRect(x: 0, y : 17, width: UIScreen.main.bounds.size.width - articleHorizontalPadding - 55, height: 34);
                let articleTitleText = UILabel(frame: articleTitleFrame);
                articleTitleText.text = article.articleTitle; // insert title text here ------ temporary
                articleTitleText.font =  UIFont(name: "SFProText-Bold",size: 16);
                
                let articleBodyFrame = CGRect(x: 0, y: 44, width: mainViewFrame.size.width, height: mainViewFrame.size.height - 50);
                let articleBodyText = UILabel(frame: articleBodyFrame);
                if (article.hasHTML == true){
                    articleBodyText.text = parseHTML(s: article.articleBody ?? "").string;
                }
                else{
                    articleBodyText.text = (article.articleBody ?? "");
                }
                articleBodyText.numberOfLines = 4;
                articleBodyText.font = UIFont(name: "SFProDisplay-Regular", size: 15);
                
                
                mainView.addSubview(articleTitleText);
                mainView.addSubview(articleBodyText);
                
                let dateTextFrame = CGRect(x: bulletinFrame.size.width - (2*articleHorizontalPadding) - 95, y : 5, width: 100, height: 25);
                let dateText = UILabel(frame: dateTextFrame);
                dateText.text = epochClass.epochToString(epoch: article.articleUnixEpoch ?? -1); // insert date here -------- temporary
                dateText.textColor = makeColor(r: 156, g: 0, b: 0);
                dateText.textAlignment = .right;
                dateText.font = UIFont(name: "SFProDisplay-Regular", size: 12);
                
                let catagoryFrame = CGRect(x: 8, y: 12, width: catagoryFrameWidth, height: 20);
                let catagory = UILabel(frame: catagoryFrame);
                catagory.text = article.articleCatagory ?? "No Cata.";
                catagory.backgroundColor = UIColor.gray;
                catagory.textAlignment = .center;
                catagory.textColor = UIColor.white;
                catagory.setRoundedEdge(corners: [.bottomRight, .bottomLeft, .topRight, .topLeft], radius: 5);
                catagory.font = UIFont(name: "SFProDisplay-Semibold", size: 12);
                
                articleButton.addSubview(dateText);
                articleButton.addSubview(mainView);
                articleButton.addSubview(catagory);
                
                articleButton.layer.shadowColor = UIColor.black.cgColor;
                articleButton.layer.shadowOpacity = 0.2;
                articleButton.layer.shadowRadius = 5;
                articleButton.layer.shadowOffset = CGSize(width: 0 , height:3);
                
                mainView.articleCompleteData = bulletinDataToarticleData(data: article);
                
                mainView.addTarget(self, action: #selector(self.openArticle), for: .touchUpInside);
                self.bulletinScrollView.addSubview(articleButton);
            }
            bulletinScrollView.contentSize = CGSize(width: bulletinFrame.size.width, height: CGFloat(currY));
            bulletinScrollView.delegate = self;
        }
    }
    
    @objc func refreshBulletin(){
      //  print("refresh");
        // add func to load data
        getBulletinArticleData();
    }
    
    func setUpFilters(){
        
        for view in filterScrollView.subviews{
            view.removeFromSuperview();
        }
        
        let buttonFrameWidth = CGFloat(80);
        let filterFrameHorizontalPadding = CGFloat(20);
        
        
        //filterFrame.size = filterScrollView.frame.size;
        filterFrame.size.height = filterScrollView.frame.size.height;
        filterFrame.size.width = buttonFrameWidth; //
        
        var originX = CGFloat(16);
        
        for buttonIndex in 0..<filterSize{
            
            filterFrame.origin.x = originX;
            let filterButton = CustomUIButton(frame: filterFrame);
            filterButton.setTitle(filterName[buttonIndex], for: .normal);
            filterButton.setTitleColor(selectedFilters[buttonIndex] ? UIColor.black : UIColor.gray, for: .normal);
            filterButton.titleLabel?.font = UIFont(name: "SFProDisplay-Regular", size: 20);
            filterButton.contentVerticalAlignment = .top;
            filterButton.sizeToFit();
            //SFProText-Bold, SFProDisplay-Regular, SFProDisplay-Semibold, SFProDisplay-Black
            
            filterFrame.size.width = filterButton.frame.size.width;
            let selectedBarHeight = CGFloat(2);
            let selectedBarFrame = CGRect(x: originX, y: filterFrame.size.height-selectedBarHeight, width: filterFrame.size.width, height: selectedBarHeight);
            let selectedBar = UIView(frame: selectedBarFrame);
            selectedBar.backgroundColor = makeColor(r: 159, g: 12, b: 12);
            selectedBar.layer.cornerRadius = 1;
            selectedBar.isHidden = !selectedFilters[buttonIndex];
            
            filterButton.isSelected = selectedFilters[buttonIndex];
            
            filterButton.articleIndex = buttonIndex;
            filterButton.addTarget(self, action: #selector(self.addFilter), for: .touchUpInside);
            
            self.filterScrollView.addSubview(selectedBar);
            self.filterScrollView.addSubview(filterButton);
            originX += filterButton.frame.size.width + filterFrameHorizontalPadding;
        }
        filterScrollView.contentSize = CGSize(width: originX, height: filterScrollView.frame.size.height);
        
        filterScrollView.layer.shadowColor = UIColor.black.cgColor;
        filterScrollView.layer.shadowOpacity = 0.1;
        filterScrollView.layer.shadowOffset = .zero;
        filterScrollView.layer.shadowRadius = 5;
        filterScrollView.layer.masksToBounds = false;
        
        filterScrollView.delegate = self;
    }
    
    override func viewDidLoad() {
        super.viewDidLoad();
        refreshControl.addTarget(self, action: #selector(refreshBulletin), for: UIControl.Event.valueChanged);
        bulletinScrollView.addSubview(refreshControl);
        bulletinScrollView.isScrollEnabled = true;
        bulletinScrollView.alwaysBounceVertical = true;
        refreshControl.beginRefreshing();
        setUpFilters();
        // set up bulletin for the first time before any filters
        getBulletinArticleData();
        
    }
    
}
