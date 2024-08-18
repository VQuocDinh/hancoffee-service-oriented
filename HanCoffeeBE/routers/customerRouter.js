import express from 'express';
import { listCustomer, updateCustomerRole } from '../controllers/customerController.js';

const customerRouter = express.Router();


customerRouter.get('/', listCustomer);
customerRouter.patch('/:id',updateCustomerRole);


export default customerRouter;