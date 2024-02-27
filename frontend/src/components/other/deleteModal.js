import React from 'react';


export default function DeleteModal({isVisible, children, onCancel, onConfirm}) {
  return (
    <div className="modal-container">
      {isVisible && (
        <div>
          <div className="overlay-rectangle"></div>
          <div className="modal-overlay">
            <div className="modal-content" >
              <div style={{alignSelf: 'center', marginBottom: 12}}> 
              Confirmar borrado
              </div>
          
              <div style={{alignSelf: 'center', marginBottom: 12, marginTop: 12}}>
              {children}
              </div>
              <button
                className="auth-button"
                onClick={onCancel}
              >
                <div className="button-content">Cancelar</div>
              </button>
              <button
                className="auth-button"
                onClick={onConfirm}
              >
                <div className="button-content">Confirmar Borrado</div>
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
