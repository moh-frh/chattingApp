import React, { useState, useEffect, useRef } from 'react';
import { useAuth } from '../context/AuthContext';
import { getAllUsers, getConversation, sendMessage, markConversationAsRead, getUnreadCount } from '../services/chatService';
import './Chat.css';

const Chat = () => {
  const { user, logout } = useAuth();
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [unreadCounts, setUnreadCounts] = useState({});
  const messagesEndRef = useRef(null);

  useEffect(() => {
    loadUsers();
    loadUnreadCounts();
    // Poll for unread counts every 3 seconds
    const interval = setInterval(loadUnreadCounts, 3000);
    return () => clearInterval(interval);
  }, [users]);

  useEffect(() => {
    if (selectedUser) {
      loadConversation();
      // Mark conversation as read when viewing
      markConversationAsRead(selectedUser.id);
      const interval = setInterval(() => {
        loadConversation();
        markConversationAsRead(selectedUser.id);
      }, 2000); // Poll every 2 seconds
      return () => clearInterval(interval);
    }
  }, [selectedUser]);

  useEffect(() => {
    // Update unread counts when messages change
    if (selectedUser) {
      loadUnreadCounts();
    }
  }, [messages]);

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  const loadUsers = async () => {
    try {
      const allUsers = await getAllUsers();
      // Filter out current user
      const otherUsers = allUsers.filter(u => u.id !== user.id);
      setUsers(otherUsers);
    } catch (error) {
      console.error('Error loading users:', error);
    }
  };

  const loadUnreadCounts = async () => {
    try {
      const counts = {};
      for (const u of users) {
        try {
          const count = await getUnreadCount(u.id);
          counts[u.id] = count;
        } catch (error) {
          counts[u.id] = 0;
        }
      }
      setUnreadCounts(counts);
    } catch (error) {
      console.error('Error loading unread counts:', error);
    }
  };

  const loadConversation = async () => {
    if (!selectedUser) return;
    try {
      const conversation = await getConversation(selectedUser.id);
      setMessages(conversation);
    } catch (error) {
      console.error('Error loading conversation:', error);
    }
  };

  const handleSendMessage = async (e) => {
    e.preventDefault();
    if (!newMessage.trim() || !selectedUser) return;

    setLoading(true);
    try {
      await sendMessage(selectedUser.id, newMessage);
      setNewMessage('');
      loadConversation();
    } catch (error) {
      console.error('Error sending message:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="chat-container">
      <div className="chat-sidebar">
        <div className="chat-header">
          <h2>Chat App</h2>
          <div className="user-info">
            <span className="username">{user?.username}</span>
            <button onClick={logout} className="logout-button">Logout</button>
          </div>
        </div>
        <div className="users-list">
          <h3>Users</h3>
          {users.map((u) => (
            <div
              key={u.id}
              className={`user-item ${selectedUser?.id === u.id ? 'active' : ''}`}
              onClick={() => setSelectedUser(u)}
            >
              <div className="user-avatar">{u.username.charAt(0).toUpperCase()}</div>
              <div className="user-details">
                <div className="user-name">{u.username}</div>
                <div className="user-email">{u.email}</div>
              </div>
              {unreadCounts[u.id] > 0 && (
                <div className="unread-badge">{unreadCounts[u.id]}</div>
              )}
            </div>
          ))}
        </div>
      </div>
      <div className="chat-main">
        {selectedUser ? (
          <>
            <div className="chat-header-main">
              <div className="selected-user-info">
                <div className="user-avatar">{selectedUser.username.charAt(0).toUpperCase()}</div>
                <div>
                  <div className="user-name">{selectedUser.username}</div>
                  <div className="user-email">{selectedUser.email}</div>
                </div>
              </div>
            </div>
            <div className="messages-container">
              {messages.map((message) => (
                <div
                  key={message.id}
                  className={`message ${message.senderId === user.id ? 'sent' : 'received'}`}
                >
                  <div className="message-content">{message.content}</div>
                  <div className="message-footer">
                    <div className="message-time">
                      {new Date(message.createdAt).toLocaleTimeString()}
                    </div>
                    {message.senderId === user.id && (
                      <div className="read-status">
                        {message.isRead ? (
                          <span className="read-icon" title="Seen">✓✓</span>
                        ) : (
                          <span className="sent-icon" title="Sent">✓</span>
                        )}
                      </div>
                    )}
                  </div>
                </div>
              ))}
              <div ref={messagesEndRef} />
            </div>
            <form onSubmit={handleSendMessage} className="message-input-form">
              <input
                type="text"
                value={newMessage}
                onChange={(e) => setNewMessage(e.target.value)}
                placeholder="Type a message..."
                className="message-input"
                disabled={loading}
              />
              <button type="submit" disabled={loading || !newMessage.trim()} className="send-button">
                Send
              </button>
            </form>
          </>
        ) : (
          <div className="no-chat-selected">
            <p>Select a user to start chatting</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Chat;


