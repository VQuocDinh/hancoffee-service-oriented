import React from 'react'
import './Download.scss'
import { assets } from '../../assets/assets'
const Download = () => {
    return (
        <div className='download' id='download'>
            <div className="download__content">
                <h2 className="download__content-heading">Better experience on mobile apps  </h2>
                <p className="download__content-desc">Lorem ipsum dolor sit amet consectetur adipisicing elit. Corporis doloribus ad eius enim officia, maiores inventore vel, non placeat, esse unde ratione corrupti modi consectetur quasi ea praesentium repellat voluptatum.</p>
                
                <div className="download__content-link">
                    <a href="#">
                        <img src={assets.downloadLink} alt="" className="download__content-link-app" />
                    </a>
                </div>

            </div>

            <div className="download__img">
                <img src={assets.androidImg} alt="" />
            </div>
        </div>
    )
}

export default Download
