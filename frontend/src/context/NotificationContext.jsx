import React, { createContext, useContext } from 'react';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Swal from 'sweetalert2';

/**
 * Notification Context Module
 *
 * Provides a unified notification service leveraging React Toastify toast alerts
 * and SweetAlert2 confirmation dialogs.
 */

const NotificationContext = createContext(null);

export const NotificationProvider = ({ children }) => {
  const showToastSuccess = (message) => {
    toast.success(message, {
      position: 'top-right',
      autoClose: 3000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
    });
  };

  const showToastError = (message) => {
    toast.error(message, {
      position: 'top-right',
      autoClose: 4000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
    });
  };

  const showConfirmDialog = async ({ title, text, confirmButtonText = 'Yes, Confirm' }) => {
    const result = await Swal.fire({
      title,
      text,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#004ac6',
      cancelButtonColor: '#737686',
      confirmButtonText,
      customClass: {
        popup: 'rounded-xl shadow-xl font-body',
        confirmButton: 'px-4 py-2 text-sm rounded-lg font-medium',
        cancelButton: 'px-4 py-2 text-sm rounded-lg font-medium',
      },
    });
    return result.isConfirmed;
  };

  return (
    <NotificationContext.Provider
      value={{
        showToastSuccess,
        showToastError,
        showConfirmDialog,
      }}
    >
      {children}
      <ToastContainer />
    </NotificationContext.Provider>
  );
};

export const useNotificationContext = () => {
  const context = useContext(NotificationContext);
  if (!context) {
    throw new Error('useNotificationContext must be used within a NotificationProvider');
  }
  return context;
};
