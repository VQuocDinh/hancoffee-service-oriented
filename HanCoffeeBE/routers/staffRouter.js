import express from 'express';
import {listStaff, updateStaffRole } from '../controllers/staffController.js';

const staffRouter = express.Router();


staffRouter.get('/', listStaff);
staffRouter.patch('/:id',updateStaffRole);


export default staffRouter;