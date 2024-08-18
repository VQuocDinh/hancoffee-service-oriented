import userModel from '../models/userModel.js';


// List all staff
const listStaff = async (req, res) => {
    try {
        const staffList = await userModel.find({role:1});
        res.json({ success: true, data: staffList });
    } catch (error) {
        res.status(500).json({ success: false, message: 'Failed to fetch staff', error });
    }
};

const updateStaffRole = async (req, res) => {
    const { id } = req.params;
    const { role } = req.query;
  
    const user = await userModel.findById(id);
  
    if (user) {
      user.role = role;
      await user.save();
      res.json({ success: true, message: 'Staff role updated successfully' });
    } else {
      res.status(404);
      throw new Error('Staff not found');
    }
  };

export { listStaff,updateStaffRole };
