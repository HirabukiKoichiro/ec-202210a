package jp.co.example.ecommerce_a.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.ecommerce_a.domain.Order;
import jp.co.example.ecommerce_a.domain.OrderItem;
import jp.co.example.ecommerce_a.domain.OrderTopping;
import jp.co.example.ecommerce_a.domain.User;
import jp.co.example.ecommerce_a.form.InsertCartForm;
import jp.co.example.ecommerce_a.repository.OrderItemRepository;
import jp.co.example.ecommerce_a.repository.OrderRepository;
import jp.co.example.ecommerce_a.repository.OrderToppingRepository;

/**
 * Order,OrderItem,OrderToppingリポジトリを操作するサービスクラス.
 * 
 * @author moriharanariki
 *
 */
@Service
@Transactional
public class ShopCartService {
	
	@Autowired
	private HttpSession session;  
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderToppingRepository orderToppingRepository;
	
	public Order showCartList(Integer userId) {		
		Order order = orderRepository.findByUserIdAndStatus(userId, 0);
		return order;
	}
	
	
	public void insertItem(InsertCartForm insertCartForm) {
		User user = (User) session.getAttribute("user");
		Order order = null;
		
		//ユーザー情報が空でない場合のみ検索を行う.
		if(user != null) {	
			order = orderRepository.findByUserIdAndStatus(user.getId(), 0);
		}
		
		if(order == null) {
				//新規でオーダー情報を生成.
				Order newOrder = new Order();
				//オーダーオブジェクトのuserIdにsessionIDを利用.
				newOrder.setUserId(session.hashCode());
				newOrder.setStatus(0);
				newOrder.setTotalPrice(0);
				newOrder = orderRepository.insert(newOrder);
				//新規で作成したオーダー情報に紐づくOrderItem情報を新規で作成.
				OrderItem orderItem = new OrderItem();
				orderItem.setItemId(insertCartForm.getItemId());
				orderItem.setOrderId(newOrder.getId());
				orderItem.setQuantity(insertCartForm.getQuantity());
				orderItem.setSize(insertCartForm.getSize());
				orderItem = orderItemRepository.insert(orderItem);
				//新規で作成したOrderItem情報に紐づくOrderTopping情報を作成.
				for(Integer toppingId : insertCartForm.getToppingList() ) {
					OrderTopping orderTopping = new OrderTopping(); 
					orderTopping.setToppingId(toppingId);
					orderTopping.setOrderItemId(orderItem.getId());
					orderToppingRepository.insert(orderTopping);
				 }
				
				} else {
					//既存のオーダー情報に紐づくOrderItem情報を新規で作成.
					OrderItem orderItem = new OrderItem();
					orderItem.setItemId(insertCartForm.getItemId());
					orderItem.setOrderId(order.getId());
					orderItem.setQuantity(insertCartForm.getQuantity());
					orderItem.setSize(insertCartForm.getSize());
					orderItem = orderItemRepository.insert(orderItem);
					//新規で作成したOrderItem情報に紐づくOrderToppingテーブルを作成.
					for(Integer toppingId : insertCartForm.getToppingList() ) {
						OrderTopping orderTopping = new OrderTopping(); 
						orderTopping.setToppingId(toppingId);
						orderTopping.setOrderItemId(orderItem.getId());
						orderToppingRepository.insert(orderTopping);
					}
				}
		}

	
	public void deleteItem(Integer orderItemId) {
		orderItemRepository.deleteById(orderItemId);
	}
}