import React from 'react';

export default function CircleButton({style, url, onClickButton}){

  const handleMouseEnter = (button) => {
    button.target.style.transform = 'scale(1.05)'; 
    button.target.style.filter = 'brightness(90%)'; 
  };

  const handleMouseLeave = (button) => {
    button.target.style.transform = 'scale(1)'; 
    button.target.style.filter = 'brightness(100%)'; 
  };

  const finalStyle = {...style, ...styleButton}

  return (
    <button style={{background: `url('`+url+`')`, ...finalStyle}} onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave} onClick={onClickButton}/>
  );
}

const styleButton = {
    backgroundSize: 'contain',
    backgroundRepeat: 'no-repeat',
    color: 'white',
    height: 290,
    width: 300,
    border: 'transparent',
    cursor: 'PointerEvent',
    marginBottom: 200,
    transition: 'transform 0.2s, filter 0.2s',
    paddingTop: 100
  
};