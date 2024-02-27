import React from 'react';

export default function PrivateCodeModal({code, onConfirm}) {

  const modalStyle = {
    marginTop: "12%",
    position: 'absolute',
    width: '35%',
    height: '35%',
    marginLeft: '35%',
    marginRight: '40%',
    alignSelf: 'center',
    zIndex: 10,
    display: 'block',
  };

  const textContainerStyle = {
    width: '100%',
    textAlign: 'center',
    color: '#fff',
  };

  const buttonContainerStyle = {
    width: '100%',
    textAlign: 'center',
  };

  return (
    <div style={modalStyle}>
      <div style={textContainerStyle}>
        <div style={{ color: 'black' }}>¡Tu partida se creó con éxito! Aquí tienes tu código</div>
        <div style={{ color: 'black', margin: 12 }} >{code}</div>
      </div>
      <div style={buttonContainerStyle}>

        <button className="auth-button" onClick={onConfirm}>
          <div className="button-content">OK</div>
        </button>
      </div>
    </div>
  );
}
