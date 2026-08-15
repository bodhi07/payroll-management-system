import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { 
  HiOutlineEnvelope, 
  HiOutlineLockClosed, 
  HiOutlineEye, 
  HiOutlineEyeSlash, 
  HiOutlineArrowRight, 
  HiOutlineShieldCheck,
  HiOutlineSparkles,
  HiOutlineBanknotes,
  HiOutlineKey
} from 'react-icons/hi2';
import { useAuth } from '../hooks/useAuth';
import { useNotificationContext } from '../context/NotificationContext';
import { ROUTES } from '../constants/routes';

/**
 * Enterprise JWT Login Page Component
 *
 * Connects with Spring Boot POST /api/v1/auth/login.
 */
const Login = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const { showToastSuccess, showToastError } = useNotificationContext();
  
  const [showPassword, setShowPassword] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm({
    defaultValues: {
      usernameOrEmail: 'agrani',
      password: 'pass1234',
    },
  });

  const onSubmit = async (data) => {
    setIsSubmitting(true);
    try {
      await login(data.usernameOrEmail, data.password);
      showToastSuccess('Authentication successful! Welcome to GlobalPay Enterprise HRMS.');
      navigate(ROUTES.DASHBOARD);
    } catch (error) {
      showToastError(error.response?.data?.message || 'Invalid credentials. Please verify your username and password.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const fillCredentials = (username, pass) => {
    setValue('usernameOrEmail', username);
    setValue('password', pass);
    showToastSuccess(`Filled credentials for ${username}`);
  };

  return (
    <div className="w-full min-h-screen grid grid-cols-1 lg:grid-cols-2 bg-[#faf8ff]">
      {/* Left Column: Rich Royal Blue Gradient Hero Panel */}
      <div className="relative bg-gradient-to-br from-[#003ea8] via-[#004ac6] to-[#2563eb] text-white p-8 lg:p-14 flex flex-col justify-between overflow-hidden">
        {/* Ambient Background Blur Graphics */}
        <div className="absolute top-0 right-0 w-96 h-96 bg-white/10 rounded-full blur-3xl -mr-20 -mt-20 pointer-events-none"></div>
        <div className="absolute bottom-0 left-0 w-80 h-80 bg-blue-400/20 rounded-full blur-2xl -ml-20 -mb-20 pointer-events-none"></div>

        {/* Top Logo */}
        <div className="relative z-10 flex items-center gap-3">
          <div className="w-10 h-10 bg-white/20 backdrop-blur-md rounded-xl flex items-center justify-center text-white border border-white/30">
            <HiOutlineBanknotes className="w-6 h-6" />
          </div>
          <span className="font-heading font-extrabold text-2xl tracking-tight">GlobalPay</span>
        </div>

        {/* Center Content Section */}
        <div className="relative z-10 my-auto py-12 max-w-xl">
          <span className="inline-block text-xs font-bold tracking-widest uppercase px-3 py-1 bg-white/10 rounded-full text-blue-100 border border-white/20 mb-4">
            ENTERPRISE HRMS & PAYROLL
          </span>
          <h1 className="font-heading font-extrabold text-4xl lg:text-5xl leading-tight tracking-tight mb-6">
            The future of global payroll is here.
          </h1>
          <p className="text-blue-100 text-base lg:text-lg leading-relaxed mb-8 font-normal">
            End-to-end Spring Boot 3 & React HRMS with statutory EPF/ETF calculations, attendance tracking, leave management, and role-based security.
          </p>

          {/* Quick Demo Login Credentials Card */}
          <div className="relative rounded-2xl p-6 bg-white/10 backdrop-blur-md border border-white/20 shadow-2xl space-y-3">
            <div className="flex items-center gap-2 text-xs font-bold uppercase tracking-wider text-blue-200">
              <HiOutlineKey className="w-4 h-4 text-amber-300" />
              <span>Quick-Fill Seeded Test Credentials</span>
            </div>
            
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 pt-2">
              <button
                type="button"
                onClick={() => fillCredentials('agrani', 'pass1234')}
                className="p-3 bg-white/20 hover:bg-white/30 rounded-xl text-left transition-colors border border-white/20"
              >
                <p className="text-xs font-extrabold text-white">Administrator / HR</p>
                <p className="text-[11px] text-blue-100 mt-0.5 font-mono">agrani / pass1234</p>
              </button>

              <button
                type="button"
                onClick={() => fillCredentials('john.doe', 'pass1234')}
                className="p-3 bg-white/20 hover:bg-white/30 rounded-xl text-left transition-colors border border-white/20"
              >
                <p className="text-xs font-extrabold text-white">Standard Staff</p>
                <p className="text-[11px] text-blue-100 mt-0.5 font-mono">john.doe / pass1234</p>
              </button>
            </div>
          </div>
        </div>

        {/* Bottom Security Footer */}
        <div className="relative z-10 pt-6 border-t border-white/10 flex items-center gap-4 text-xs text-blue-100">
          <HiOutlineShieldCheck className="w-5 h-5 text-emerald-300" />
          <span>Stateless 256-bit BCrypt JWT Session Security</span>
        </div>
      </div>

      {/* Right Column: Interactive Login Form Container */}
      <div className="flex flex-col items-center justify-center p-8 lg:p-16 bg-[#faf8ff]">
        <div className="max-w-md w-full">
          <div className="mb-8 text-left">
            <h2 className="font-heading font-extrabold text-3xl text-[#191b23] tracking-tight">
              Welcome back
            </h2>
            <p className="text-sm font-normal text-[#737686] mt-2">
              Enter your username or email address and password to sign in.
            </p>
          </div>

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
            {/* Username or Email Field */}
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-2">
                Username or Email
              </label>
              <div className="relative">
                <HiOutlineEnvelope className="w-5 h-5 text-[#737686] absolute left-3.5 top-1/2 -translate-y-1/2" />
                <input
                  type="text"
                  {...register('usernameOrEmail', {
                    required: 'Username or email is required',
                  })}
                  placeholder="agrani or agrani@payroll.com"
                  className={`w-full bg-white border ${
                    errors.usernameOrEmail ? 'border-[#ba1a1a]' : 'border-[#c3c6d7]'
                  } rounded-xl pl-11 pr-4 py-3 text-sm text-[#191b23] placeholder-[#737686] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20 focus:border-[#004ac6] transition-all`}
                />
              </div>
              {errors.usernameOrEmail && (
                <p className="text-xs text-[#ba1a1a] mt-1 font-medium">{errors.usernameOrEmail.message}</p>
              )}
            </div>

            {/* Password Field */}
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-2">
                Password
              </label>
              <div className="relative">
                <HiOutlineLockClosed className="w-5 h-5 text-[#737686] absolute left-3.5 top-1/2 -translate-y-1/2" />
                <input
                  type={showPassword ? 'text' : 'password'}
                  {...register('password', { required: 'Password is required' })}
                  placeholder="••••••••"
                  className={`w-full bg-white border ${
                    errors.password ? 'border-[#ba1a1a]' : 'border-[#c3c6d7]'
                  } rounded-xl pl-11 pr-11 py-3 text-sm text-[#191b23] placeholder-[#737686] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20 focus:border-[#004ac6] transition-all`}
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3.5 top-1/2 -translate-y-1/2 text-[#737686] hover:text-[#191b23]"
                >
                  {showPassword ? (
                    <HiOutlineEyeSlash className="w-5 h-5" />
                  ) : (
                    <HiOutlineEye className="w-5 h-5" />
                  )}
                </button>
              </div>
              {errors.password && (
                <p className="text-xs text-[#ba1a1a] mt-1 font-medium">{errors.password.message}</p>
              )}
            </div>

            {/* Primary Submit Button */}
            <button
              type="submit"
              disabled={isSubmitting}
              className="w-full py-3.5 px-6 bg-[#004ac6] hover:bg-[#2563eb] text-white font-semibold text-sm rounded-xl shadow-md flex items-center justify-center gap-2 transition-all active:scale-[0.98] disabled:opacity-50"
            >
              <span>{isSubmitting ? 'Authenticating with Spring Boot...' : 'Sign In to Dashboard'}</span>
              <HiOutlineArrowRight className="w-4 h-4" />
            </button>
          </form>

          {/* Footer Assistance */}
          <div className="mt-10 text-center space-y-2">
            <p className="text-xs text-[#737686]">
              Need a new enterprise user account?{' '}
              <Link to={ROUTES.REGISTER} className="font-bold text-[#004ac6] hover:underline">
                Register New User
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
