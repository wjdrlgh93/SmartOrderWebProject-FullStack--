
import { Link } from 'react-router-dom'


import '../css/index/indexPage.css';

import bgImage from '../../public/images/INDEX/bg.png';
import part1 from '../../public/images/INDEX/part_w1.png';
import part2 from '../../public/images/INDEX/part_w2.png';
import fixbar1 from '../../public/images/INDEX/part1.png';
import fixbar2 from '../../public/images/INDEX/part2.png';
import people from '../../public/images/INDEX/part3.png';
import run from '../../public/images/INDEX/run.png';

import { useEffect, useState } from 'react';



const IndexPage = () => {


    const [mousePosition, setMousePosition] = useState({ x: 0, y: 0 });

    useEffect(() => {
        const handleMouseMove = (event) => {
            // get viewPort Size
            const centerX = window.innerWidth / 2;
            const centerY = window.innerHeight / 2;

            const relativeX = (event.clientX - centerX) / centerX;
            const relativeY = (event.clientY - centerY) / centerY;
            setMousePosition({ x: relativeX, y: relativeY });

        };
        // widnow -> add MouseMoveListner
        window.addEventListener('mousemove', handleMouseMove);

        // when component disapper -> removeListener
        return () => {
            window.removeEventListener('mousemove', handleMouseMove);
        };
    }, []);

    const depthConfig = {
        part1: 105,   // 배경 (덜 움직임)
        part2: 10,  // 전경 (더 많이 움직임)
        title: 35,  // 텍스트 (가장 많이 움직임)
        run: 55,
        runChar: 80

    };
       const getTransformStyleChar = (depth) => {
   
        const translateX = 0;
        const translateY = -mousePosition.x * (depth*4);

        return {
            transform: `translate(${translateX}px, ${translateY}px)`,
        };
    };


    const getTransformStyle = (depth) => {

        const translateX = -mousePosition.x * depth;
        const translateY = -mousePosition.y * depth;

        return {
            transform: `translate(${translateX}px, ${translateY}px)`,
        };
    };

    




    return (
        <div className="title-container" style={{ backgroundImage: `url(${bgImage})` }}>
            <div className="title-video-container">
                <video autoPlay loop muted playsInline className='background-video'>
                    <source src='/videos/bg.mp4' type='video/mp4'/>
                    Your browser does not support the video tag.
                </video>
                <div className="video-overlay"></div>
                <div className="second-colorLayer">
                    <img src={bgImage} alt="bgcolor"/>
                </div>
            </div>

            
            <div className="imgfix">
                <img src={fixbar1} alt="bar1" className='fixbar1'/>
                   <img
                src={run}
                alt="Running"
                className="parallax-layer layer-5"
                style={getTransformStyleChar(depthConfig.runChar)} 
            />
            </div>
            <div className="title-video-container2">
                <div className="video-overlay2"></div>
                <video autoPlay loop muted playsInline className='background-video'>
                    <source src='/videos/secound_run.mp4' type='video/mp4'/>
                    Your browser does not support the video tag.
                </video>
                </div>
                <div>TEST</div>
         
          
            <img
                src={part1}
                alt="Runner group"
                className="parallax-layer layer-1"
                style={getTransformStyle(depthConfig.part1)}
            />
            <img
                src={part2}
                alt="Couple running"
                className="parallax-layer layer-2"
                style={getTransformStyle(depthConfig.part2)}
            />
            <div className="human-part">
                <img src={people} alt="run"
                className="parallax-layer layer-4"
                style={getTransformStyle(depthConfig.run)}
                 />
            </div>
            <div
                className="title-text layer-3"
                style={getTransformStyle(depthConfig.title)}
            >
                <Link to={"/store"}>
                    <h1>:: ENTER ::</h1>
                </Link >
                <p>(주)같이달리죠 컴퍼니</p>
            </div>
        </div >
    )
}

export default IndexPage