import React from 'react';

export default function SearchingModal({ type, onCancel, onConfirm, onRepeat }) {
  const gifUrl = 'https://media1.giphy.com/media/cIbJ5bBlNLxynVGYzb/giphy.gif?cid=ecf05e47q6npes7f2en1dfeh446520m2z98gb5gvguuxlkf6&ep=v1_gifs_search&rid=giphy.gif&ct=g'

  const modalStyle = {
    position: 'absolute',
    width: '35%',
    height: '35%',
    marginLeft: '35%',
    marginRight: '40%',
    alignSelf: 'center',
    zIndex: 10,
    display: type !== null ? 'block' : 'none',
  };

  const gifContainerStyle = {
    position: 'relative',
    alignSelf: 'center',
    marginBottom: 12,
    marginTop: 12,
    boxShadow: '0px 0px 10px 0px rgba(0, 0, 0, 0.75)', // Añadido para la sombra
  };

  const textContainerStyle = {
    position: 'absolute',
    top: 8,
    left: 0,
    width: '100%',
    textAlign: 'center',
    color: 'white',
  };

  const buttonContainerStyle = {
    position: 'absolute',
    bottom: 8,
    left: 0,
    width: '100%',
    textAlign: 'center',
  };

  let text = ""
  let button = <></>

  if (type === "SEARCHING") {
    text = "¡Estamos buscando una partida! ¿Desea cancelar?"
    button = <>
      <button className="auth-button" onClick={onCancel}>
        <div className="button-content">Cancelar</div>
      </button>
    </>
  } else if (type === "FOUND_MATCH") {
    text = "¡Partida encontrada! ¿Desea unirse?"
    button = <>
      <button className="auth-button" onClick={onCancel}>
        <div className="button-content">Cancelar</div>
      </button>
      <button style={{ marginLeft: 12 }} className="auth-button" onClick={onConfirm}>
        <div className="button-content">Unirse</div>
      </button>
    </>
  } else if (type === "SUGGEST_CREATE") {
    text = "¡Vaya! Parece que no se encontró partida, ¿deseas crear una?"
    button = <>
      <button className="auth-button" onClick={onCancel}>
        <div className="button-content">CANCELAR</div>
      </button>
      <button className="auth-button" onClick={onRepeat}>
        <div className="button-content">BUSCAR DE NUEVO</div>
      </button>
      <button className="auth-button" onClick={onConfirm}>
        <div className="button-content">CREAR PARTIDA</div>
      </button>
    </>
  }

  return (
    <div style={modalStyle}>
      {type !== null && (
        <div style={gifContainerStyle}>
          <img src={gifUrl} alt="Buscando partida" style={{ width: '100%', height: 'auto' }} />
          <div style={textContainerStyle}>{text}</div>
          <div style={buttonContainerStyle}>{button}</div>
        </div>
      )}
    </div>
  );
}
