import React from "react";


export default function ChatMessage({ username, message }) {

  const SIZE_ROW = 25;

  function stringToColor(str) {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
      hash = str.charCodeAt(i) + ((hash << 5) - hash);
    }
    const color = Math.floor(Math.abs((Math.sin(hash) * 10000) % 1) * 16777215).toString(16);
    return `#${'000000'.substring(0, 6 - color.length)}${color}`;
  }

  function parseMessage() {
    let res = "";
    let i = 0;
    do {
      res += message.substring(i, i + SIZE_ROW) + "\n"
      i += SIZE_ROW;
    } while (i < message.length);

    return res;
  }

  return (<div style={{ color: username ? stringToColor(username) : "#fff" }}>
    <strong>{username}:</strong> {parseMessage()}
  </div>
  );
}