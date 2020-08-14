//
//  zoomableImageViewController.swift
//  AHS20
//
//  Created by Richard Wei on 8/13/20.
//  Copyright © 2020 AHS. All rights reserved.
//

import Foundation
import UIKit

class zoomableImageViewController: UIViewController, UIScrollViewDelegate{
    
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var imageView: UIImageView!
    
    var image: UIImage?;
    
    @IBAction func dismissAction(_ sender: UIButton) {
        UIImpactFeedbackGenerator(style: .light).impactOccurred();
        dismiss(animated: true);
    }
    
    override func viewDidLoad() {
        super.viewDidLoad();
      //  scrollView.bottomAnchor.constraint(equalToSystemSpacingBelow: view.bottomAnchor, multiplier: 1).isActive = true;
        if (image == nil){
            dismissAction(UIButton());
        }
        scrollView.alwaysBounceVertical = false;
        scrollView.alwaysBounceHorizontal = false;
        scrollView.minimumZoomScale = 1.0;
        scrollView.maximumZoomScale = 6.0;
        scrollView.backgroundColor = UIColor.lightGray;
        imageView.image = image;
    }
    
    func viewForZooming(in scrollView: UIScrollView) -> UIView? {
        return imageView;
    }
    
}
