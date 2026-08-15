import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { 
  HiOutlineUser, 
  HiOutlineEnvelope, 
  HiOutlineLockClosed, 
  HiOutlineEye, 
  HiOutlineEyeSlash, 
  HiOutlineArrowRight, 
  HiOutlineShieldCheck,
  HiOutlineSparkles,
  HiOutlineBanknotes,
  HiOutlineBriefcase
} from 'react-icons/hi2';
import { authApi } from '../api/authApi';
import { useNotificationContext } from '../context/NotificationContext';
import { ROUTES } from '../constants/routes';

/**
 * Enterprise User Registration Page Component
 *
 * Converts Spring Boot `/api/v1/auth/register` endpoints into a pixel-perfect React implementation.
 * Left panel: Rich royal blue gradient hero banner, product mockup, and security guarantees.
 * Right panel: React Hook Form capturing username, email, password, confirm password, and security roles.
 */
const Register = () => {
  const navigate = useNavigate();
  const { showToastSuccess, showToastError } = useNotificationContext();
  
  const [showPassword, setShowPassword] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm({
    defaultValues: {
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
      role: 'ROLE_EMPLOYEE',
    },
  });

  const passwordValue = watch('password');

  const onSubmit = async (data) => {
    setIsSubmitting(true);
    try {
      // Structure DTO payload matching Spring Boot RegisterRequestDTO
      const payload = {
        username: data.username,
        email: data.email,
        password: data.password,
        roles: [data.role],
      };

      await authApi.register(payload);
      showToastSuccess('Registration successful! Please sign in with your new credentials.');
      navigate(ROUTES.LOGIN);
    } catch (error) {
      const errMsg = error.response?.data?.message || 'Registration failed. Username or email may already exist.';
      showToastError(errMsg);
    } finally {
      setIsSubmitting(false);
    }
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
        <div className="relative z-10 my-auto py-8 max-w-xl">
          <span className="inline-block text-xs font-bold tracking-widest uppercase px-3 py-1 bg-white/10 rounded-full text-blue-100 border border-white/20 mb-4">
            ENTERPRISE ACCOUNT CREATION
          </span>
          <h1 className="font-heading font-extrabold text-4xl lg:text-5xl leading-tight tracking-tight mb-6">
            Join the future of global payroll.
          </h1>
          <p className="text-blue-100 text-base lg:text-lg leading-relaxed mb-8 font-normal">
            Create your enterprise account to streamline payroll operations, manage global employee records, and access institutional-grade reporting.
          </p>

          {/* Feature Badges Container */}
          <div className="grid grid-cols-2 gap-4">
            <div className="p-4 rounded-xl bg-white/10 backdrop-blur-md border border-white/20">
              <HiOutlineShieldCheck className="w-6 h-6 text-emerald-300 mb-2" />
              <h4 className="font-heading font-bold text-sm">BCrypt Encrypted</h4>
              <p className="text-xs text-blue-100 mt-1">Institutional security compliance</p>
            </div>
            <div className="p-4 rounded-xl bg-white/10 backdrop-blur-md border border-white/20">
              <HiOutlineSparkles className="w-6 h-6 text-amber-300 mb-2" />
              <h4 className="font-heading font-bold text-sm">Role-Based Access</h4>
              <p className="text-xs text-blue-100 mt-1">Admin, HR, & Employee roles</p>
            </div>
          </div>
        </div>

        {/* Bottom Security Guarantee */}
        <div className="relative z-10 pt-4 border-t border-white/10 flex items-center justify-between text-xs text-blue-100">
          <span>GlobalPay HRMS System v1.0</span>
          <span className="flex items-center gap-1">
            <HiOutlineShieldCheck className="w-4 h-4 text-emerald-400" />
            256-Bit SSL Encrypted
          </span>
        </div>
      </div>

      {/* Right Column: Interactive Registration Form Container */}
      <div className="flex flex-col items-center justify-center p-8 lg:p-12 bg-[#faf8ff]">
        <div className="max-w-md w-full">
          {/* Header */}
          <div className="mb-6 text-left">
            <h2 className="font-heading font-extrabold text-3xl text-[#191b23] tracking-tight">
              Create an Account
            </h2>
            <p className="text-sm font-normal text-[#737686] mt-2">
              Enter your organizational details to register.
            </p>
          </div>

          {/* Form */}
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            {/* Username Field */}
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1.5">
                Username
              </label>
              <div className="relative">
                <HiOutlineUser className="w-5 h-5 text-[#737686] absolute left-3.5 top-1/2 -translate-y-1/2" />
                <input
                  type="text"
                  {...register('username', {
                    required: 'Username is required',
                    minLength: { value: 3, message: 'Username must be at least 3 characters' },
                  })}
                  placeholder="johndoe"
                  className={`w-full bg-white border ${
                    errors.username ? 'border-[#ba1a1a]' : 'border-[#c3c6d7]'
                  } rounded-xl pl-11 pr-4 py-2.5 text-sm text-[#191b23] placeholder-[#737686] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20 focus:border-[#004ac6] transition-all`}
                />
              </div>
              {errors.username && (
                <p className="text-xs text-[#ba1a1a] mt-1 font-medium">{errors.username.message}</p>
              )}
            </div>

            {/* Email Field */}
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1.5">
                Work Email Address
              </label>
              <div className="relative">
                <HiOutlineEnvelope className="w-5 h-5 text-[#737686] absolute left-3.5 top-1/2 -translate-y-1/2" />
                <input
                  type="email"
                  {...register('email', {
                    required: 'Email address is required',
                    pattern: {
                      value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                      message: 'Invalid email address format',
                    },
                  })}
                  placeholder="john.doe@company.com"
                  className={`w-full bg-white border ${
                    errors.email ? 'border-[#ba1a1a]' : 'border-[#c3c6d7]'
                  } rounded-xl pl-11 pr-4 py-2.5 text-sm text-[#191b23] placeholder-[#737686] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20 focus:border-[#004ac6] transition-all`}
                />
              </div>
              {errors.email && (
                <p className="text-xs text-[#ba1a1a] mt-1 font-medium">{errors.email.message}</p>
              )}
            </div>

            {/* Security Role Selector */}
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1.5">
                Security Role
              </label>
              <div className="relative">
                <HiOutlineBriefcase className="w-5 h-5 text-[#737686] absolute left-3.5 top-1/2 -translate-y-1/2" />
                <select
                  {...register('role')}
                  className="w-full bg-white border border-[#c3c6d7] rounded-xl pl-11 pr-4 py-2.5 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20 focus:border-[#004ac6] transition-all"
                >
                  <option value="ROLE_EMPLOYEE">Employee (Standard Access)</option>
                  <option value="ROLE_HR">HR Specialist (HR & Employee Management)</option>
                  <option value="ROLE_ADMIN">System Administrator (Full Privileges)</option>
                </select>
              </div>
            </div>

            {/* Password Field */}
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1.5">
                Password
              </label>
              <div className="relative">
                <HiOutlineLockClosed className="w-5 h-5 text-[#737686] absolute left-3.5 top-1/2 -translate-y-1/2" />
                <input
                  type={showPassword ? 'text' : 'password'}
                  {...register('password', {
                    required: 'Password is required',
                    minLength: { value: 6, message: 'Password must be at least 6 characters' },
                  })}
                  placeholder="••••••••"
                  className={`w-full bg-white border ${
                    errors.password ? 'border-[#ba1a1a]' : 'border-[#c3c6d7]'
                  } rounded-xl pl-11 pr-11 py-2.5 text-sm text-[#191b23] placeholder-[#737686] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20 focus:border-[#004ac6] transition-all`}
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

            {/* Confirm Password Field */}
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1.5">
                Confirm Password
              </label>
              <div className="relative">
                <HiOutlineLockClosed className="w-5 h-5 text-[#737686] absolute left-3.5 top-1/2 -translate-y-1/2" />
                <input
                  type={showPassword ? 'text' : 'password'}
                  {...register('confirmPassword', {
                    required: 'Please confirm your password',
                    validate: (val) => val === passwordValue || 'Passwords do not match',
                  })}
                  placeholder="••••••••"
                  className={`w-full bg-white border ${
                    errors.confirmPassword ? 'border-[#ba1a1a]' : 'border-[#c3c6d7]'
                  } rounded-xl pl-11 pr-4 py-2.5 text-sm text-[#191b23] placeholder-[#737686] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20 focus:border-[#004ac6] transition-all`}
                />
              </div>
              {errors.confirmPassword && (
                <p className="text-xs text-[#ba1a1a] mt-1 font-medium">{errors.confirmPassword.message}</p>
              )}
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              disabled={isSubmitting}
              className="w-full py-3.5 px-6 bg-[#004ac6] hover:bg-[#2563eb] text-white font-semibold text-sm rounded-xl shadow-md flex items-center justify-center gap-2 transition-all active:scale-[0.98] disabled:opacity-50 mt-2"
            >
              <span>{isSubmitting ? 'Registering Account...' : 'Create Enterprise Account'}</span>
              <HiOutlineArrowRight className="w-4 h-4" />
            </button>
          </form>

          {/* Footer Assistance */}
          <div className="mt-6 text-center">
            <p className="text-xs text-[#737686]">
              Already have an enterprise account?{' '}
              <Link to={ROUTES.LOGIN} className="font-bold text-[#004ac6] hover:underline">
                Sign In
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Register;
