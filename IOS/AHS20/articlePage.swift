//
//  File.swift
//  AHS20
//
//  Created by Richard Wei on 4/21/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit

class articlePageViewController: UIViewController, UIScrollViewDelegate{
    /*@IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var articleText: UILabel!
    @IBOutlet weak var imageScrollView: UIScrollView!
    @IBOutlet weak var imagePageControl: UIPageControl!
    @IBOutlet weak var whiteBackground: UIImageView!*/    //@IBOutlet weak var notificationBellButton: UIButton!
   // @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var imageScrollView: UIScrollView!
    @IBOutlet weak var imagePageControl: UIPageControl!
    @IBOutlet weak var whiteBackground: UIImageView!
    @IBOutlet weak var articleText: UILabel!
    @IBOutlet weak var savedButton: UIButton!
    @IBOutlet weak var mainScrollView: UIScrollView!
    
    
    
    var contentWidth: CGFloat = 0.0
    var imageSize = 3;
    
    @objc func saveArticle(sender: CustomUIButton){
        print("Bookmark");
    }
    
    
    @IBAction func exitPopup(_ sender: UIButton) {
            dismiss(animated: true);
    }
    
    
    // ------------
    // TODO: Fix issue where long text gets cut off
    // TODO: Fix issue where the imagescrollview doesn't allow you to go to the third image on real devices
    // TODO: Fix issue on Storyboard where the Bookmark button gets blurry - use the system bookmark image and put a circular background behind it
    // ------------
    override func viewDidLoad() {
        super.viewDidLoad();
        
        mainScrollView.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;
        
        savedButton.addTarget(self, action: #selector(saveArticle), for: .touchUpInside);
    
        // TODO: Fix issue where long text gets cut off
        articleText.text = "Opera nullo ratio an libet de tangi sequi. Im me gurgitem quadrati connivet experiar de fatendum quatenus. Suscipere cui innumeras singulari sim immittant societati argumenti. Proponere concipiam evidentia purgantur to ne vereorque ac. Corpo to nihil nolim prima et et ad. Verti est supra imo omnem sic sitas Cum facultate supersunt objective spectatum nul meditatio jam suo. Possum sacras initia rea ita. Illud ferre sub gustu tes agi solum. Rem cogitari mutuatur pla attentum. Me quandiu ac is id intueor ineptum. Prorsus fraudem certius agnosco eo sirenes dicitur gi. Nulli tangi is omnem ei ex at. Vos conservet via existendi nia conflatum admiserim eas dubitavit. To et existat quosdam equidem ac affirmo formali accepit.Viderer totaque ineptum id ac et. Eaedem vi fueram to du at mentes. Confirmari praesertim praecipuis ex externarum ac at satyriscos to. Vitae etc lumen lus solam novas lapis. Ha exhibentur occasionem credidique si sufficeret. Creatus idearum admonet reducit ne si in quandam cognitu. Quid veat mens eas cui rem.Hactenus animalia existimo potentia rea ita perpauca existens. Existimo reductis nonnihil fal inficior sui his via.Opera nullo ratio an libet de tangi sequi. Im me gurgitem quadrati connivet experiar de fatendum quatenus. Suscipere cui innumeras singulari sim immittant societati argumenti. Proponere concipiam evidentia purgantur to ne vereorque ac. Corpo to nihil nolim prima et et ad. Verti est supra imo omnem sic sitas Cum facultate supersunt objective spectatum nul meditatio jam suo. Possum sacras initia rea ita. Illud ferre sub gustu tes agi solum. Rem cogitari mutuatur pla attentum. Me quandiu ac is id intueor ineptum. Prorsus fraudem certius agnosco eo sirenes dicitur gi. Nulli tangi is omnem ei ex at. Vos conservet via existendi nia conflatum admiserim eas dubitavit. To et existat quosdam equidem ac affirmo formali accepit.Viderer totaque ineptum id ac et. Eaedem vi fueram to du at mentes. Confirmari praesertim praecipuis ex externarum ac at satyriscos to. Vitae etc lumen lus solam novas lapis. Ha exhibentur occasionem credidique si sufficeret. Creatus idearum admonet reducit ne si in quandam cognitu. Quid veat mens eas cui rem.Hactenus animalia existimo potentia rea ita perpauca existens. Existimo reductis nonnihil fal inficior sui his via."
        
        //rounded corners (bottom corners-> [.layerMaxXMaxYCorner, .layerMinXMaxYCorner])
        whiteBackground.layer.maskedCorners = [.layerMinXMinYCorner, .layerMaxXMinYCorner]
        
        //horizontal image scroll view
        imageScrollView.delegate = self
        
        for imageIndex in 0..<imageSize{
            let imageToDisplay = UIImage(named: "\(imageIndex).png")
            let imageView = UIImageView(image: imageToDisplay)
            
            imageScrollView.addSubview(imageView)
            
            let xCoordinate = view.frame.midX + view.frame.width * CGFloat(imageIndex) // use UIScreen.main.bounds.width instead - view.frame.width is constant size across all devices while UIScreen is different
            
            contentWidth += view.frame.width
            
            imageView.frame = CGRect(x: xCoordinate, y: view.frame.height/2, width: 100, height: 100)
        }
        
        imageScrollView.contentSize = CGSize(width: contentWidth, height: view.frame.height)
    }
    
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        imagePageControl.currentPage = Int(imageScrollView.contentOffset.x / CGFloat(414))
    }
}
