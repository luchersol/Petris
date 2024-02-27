import React, { useEffect, useState } from "react";
import { ImCancelCircle } from "react-icons/im";
import { MdOutlineInsertComment } from "react-icons/md";
import getItem from "../../util/getItem.js";
import saveItem from "../../util/saveItem.js";
import ChatMessage from "./chatMessage";

export default function Chat({match_id, user_id, jwt}){

    const [visible, setVisible] = useState(false)
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState('');
    const openComments = () => setVisible(true);
    const closeComments = () => setVisible(false);

    useEffect(() => {
        let interval = setInterval(() => {
            getItem(`/petris/comment?match_id=${match_id}`, messages, setMessages, jwt);
        }, 2000);

        return () => clearInterval(interval);
    }, [])
    
    const handleSendMessage = () => {
        if (message.trim() !== '') {
            
            const comment = {
                commentDate: new Date(),
                message
            }
            saveItem(`/petris/comment?match_id=${match_id}&user_id=${user_id}`, "POST", comment, jwt);
            setMessage('');
        }
    };    

    return (<>
        {!visible && <MdOutlineInsertComment onClick={openComments} color="black" size={30} style={{position: "absolute", top: "12%", right: "3%"}} />}
        {visible && 
        <div>
            <ImCancelCircle onClick={closeComments} color="red" size={30} style={{position: "fixed", top: "12%", right: "3%", zIndex: 2}}/>
            <div style={{borderRadius: "5%", position: "fixed", bottom: "1%", right: "1%", display: "flex", flexDirection: "column-reverse", zIndex: 1, height: "88%", width:"23%", overflowY: "auto", backgroundColor: "black"}}>
                <div style={{padding: "10px", marginBottom: 30, marginLeft: 10}}>
                    {messages.map((msg, index) => (
                        <ChatMessage key={index} username={`${msg.sentBy.user.username}`} message={msg.message} />
                    ))}
                </div>
                <div style={{position: "fixed", bottom: 15, right: 60}}>
                    <input
                        type="text"
                        placeholder="Type your message..."
                        value={message}
                        onChange={(e) => setMessage(e.target.value)}
                        onKeyDown={(event) => {if(event.key === "Enter")  handleSendMessage()}}
                    />
                    <button onClick={handleSendMessage}>Send</button>
                </div>
            </div>
        </div>
        }
    </>);
}