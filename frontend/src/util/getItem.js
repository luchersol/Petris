const {isEqual} = require('lodash');

export default function getItem(url, item, setItem, jwt) {
    
    fetch(url, {
        headers: {
            "Authorization": `Bearer ${jwt}`,
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        })
        .then(response => response.json())
        .then(json => {
            if(!isEqual(item, json) && Object.keys(json).length !== 0){
                setItem(json);
            }
        })
        .catch((message) => alert(message));

}