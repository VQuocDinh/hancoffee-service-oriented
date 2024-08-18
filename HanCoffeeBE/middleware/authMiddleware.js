const jwt = require('jsonwebtoken');

const authorize = (roles = []) => {
  if (typeof roles === 'number') {
    roles = [roles];
  }

  return (req, res, next) => {
    const token = req.headers.authorization && req.headers.authorization.split(' ')[1];
    if (!token) {
      return res.status(401).json({ message: 'Access Denied. No token provided.' });
    }

    try {
      const decoded = jwt.verify(token, 'your_jwt_secret');
      if (!roles.length || roles.includes(decoded.role)) {
        req.user = decoded;
        next();
      } else {
        return res.status(403).json({ message: 'Forbidden' });
      }
    } catch (error) {
      console.error(error);
      return res.status(401).json({ message: 'Invalid Token' });
    }
  };
};

module.exports = authorize;
