import api from './api';

export const getAllUsers = async () => {
  const response = await api.get('/api/users');
  return response.data;
};

export const getConversation = async (userId) => {
  const response = await api.get(`/api/chat/conversation/${userId}`);
  return response.data;
};

export const sendMessage = async (receiverId, content) => {
  const response = await api.post('/api/chat/send', {
    receiverId,
    content
  });
  return response.data;
};

export const getUnreadMessages = async () => {
  const response = await api.get('/api/chat/unread');
  return response.data;
};

export const markAsRead = async (messageId) => {
  const response = await api.put(`/api/chat/read/${messageId}`);
  return response.data;
};

export const markConversationAsRead = async (userId) => {
  const response = await api.put(`/api/chat/read-conversation/${userId}`);
  return response.data;
};

export const getUnreadCount = async (userId) => {
  const response = await api.get(`/api/chat/unread-count/${userId}`);
  return response.data;
};


