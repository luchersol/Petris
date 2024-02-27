import { useEffect, useState } from "react";

export default function saveItem(url, method = "POST", body, jwt, setMessage, setVisible) {
    
    fetch(url, {
        method,
        headers: {
            Authorization: `Bearer ${jwt}`,
            Accept: "application/json",
            "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
        })
        .then((response) => response.json())
        .then((json) => {
            if (json.message && setMessage && setVisible) {
                setMessage(json.message);
                setVisible(true);
            } 
        })
    
}