import React, { forwardRef, useEffect, useImperativeHandle, useRef, useState } from 'react'

const SilderInner = forwardRef(({ children, silderInterval = 2000 }, ref) => {

    const sliderInnerRef = useRef(null);
    const [currentIndex, setCurrentIndex] = useState(0);
    const slides = React.Children.toArray(children);
    const sliderCount = slides.length;
    const [sliderWidth, setSliderWidth] = useState(0);


    const handlePrev = (e) => {
        const prevIndex = currentIndex === 0 ? sliderCount : currentIndex - 1;
        setCurrentIndex(prevIndex);
    };

    const handleNext = (e) => {
        setCurrentIndex(currentIndex + 1);
    };
    const handleThumbnailClick = (index) => {
        setCurrentIndex(index);
    };

    useImperativeHandle(ref, () => ({
        prev: handlePrev,
        next: handleNext,
    }));

    const updateSliderWidth = () => {
        if (sliderInnerRef.current) {
            const firstSlide = sliderInnerRef.current.querySelector(".slider");
            if (firstSlide) {
                setSliderWidth(firstSlide.clientWidth);
            }
        }
    };


    useEffect(() => {
        const intervalId = setInterval(() => {
            setCurrentIndex(prevIndex => prevIndex + 1);
        }, silderInterval); // 


        return () => clearInterval(intervalId);
    }, [silderInterval]);



    useEffect(() => {

        updateSliderWidth();
        

        window.addEventListener('resize', updateSliderWidth);

        // 청소
        return () => {
            window.removeEventListener('resize', updateSliderWidth);
        };
    }, []); 



    useEffect(() => {
        const inner = sliderInnerRef.current;
        if (!inner || sliderWidth === 0) { return; }

        const transitionDuration = '0.6s';

        if (currentIndex < sliderCount) {
            inner.style.transition = `transform ${transitionDuration}`;
            inner.style.transform = `translateX(-${sliderWidth * currentIndex}px)`;
        }
        else if (currentIndex === sliderCount) {
            inner.style.transition = `transform ${transitionDuration}`;
            inner.style.transform = `translateX(-${sliderWidth * currentIndex}px)`;

            // 마지막 복사본에 도달하면 0.6초 뒤에 순식간에 처음으로 이동
            setTimeout(() => {
                inner.style.transition = 'none';
                inner.style.transform = 'translateX(0px)';
                setCurrentIndex(0);
            }, 600); 
        }
    }, [currentIndex, sliderCount, sliderWidth]);

    const sliderClone =
        React.cloneElement
            (slides[0], { key: 'clone-0', className: `${slides[0].props.className} clone-slide` });

    const displayIndex = (currentIndex % sliderCount) + 1;

    const dotElements = Array.from({ length: sliderCount }).map((_, index) => (
        <span
            key={`dot-${index}`}
            className={`slider__dot ${index === (currentIndex % sliderCount) ? 'active' : ''}`}
            onClick={() => handleThumbnailClick(index)}
        />
    ));


    return (
        <>
            <div className="slider__count">
                <span className="current">{displayIndex}</span>
                <span className="separator"> / </span>
                <span className="total">{sliderCount}</span>
            </div>
            <div className="slider__pagination">
                {dotElements}
            </div>
            <div className="slider__inner" ref={sliderInnerRef}>
                {children}
                {sliderClone}
            </div>
        </>
    )
})
export default SilderInner