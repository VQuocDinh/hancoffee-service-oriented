
import User from '../models/userModel.js';


// List all staff
const listCustomer = async (req, res) => {
    try {
        const customerList = await User.find({role:2});
        res.json({ success: true, data: customerList });
    } catch (error) {
        res.status(500).json({ success: false, message: 'Failed to fetch customer', error });
    }
};

const updateCustomerRole = async (req, res) => {
    const { id } = req.params;
    const { role } = req.query;
  
    const user = await User.findById(id);
  
    if (user) {
      user.role = role;
      await user.save();
      res.json({ success: true, message: 'Customer role updated successfully' });
    } else {
      res.status(404);
      throw new Error('Customer not found');
    }
  };

export { listCustomer,updateCustomerRole };
