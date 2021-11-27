//YOU MAY NOT MODIFY THIS DOCUMENT
#ifndef DEQUE_INTERFACE_H
#define DEQUE_INTERFACE_H
#include <string>
using std::string;

template<typename T>
class DequeInterface
{
public:
	static const size_t DEFAULT_CAPACITY = 4;

	DequeInterface(void) {}
	~DequeInterface(void) {}

	/** Insert item at front of deque */
	virtual void push_front(const T& value) = 0;

	/** Insert item at rear of deque */
	virtual void push_back(const T& value) = 0;

	/** Remove the front item of the deque */
	virtual void pop_front(void) = 0;

	/** Remove the rear item of the deque */
	virtual void pop_back(void) = 0;

	/** Return the front item of the deque (Do not remove) */
	virtual T& front(void) = 0;

	/** Return the rear item of the deque (Do not remove) */
	virtual T& back(void) = 0;

	/** Return the number of items in the deque */
	virtual size_t size(void) const = 0;

	/** Return true if deque is empty */
	virtual bool empty(void) const = 0;

	/** Return item in deque at index (0 based) */
	virtual T& at(size_t index) = 0;

	/** Return the deque items */
	virtual string toString() const = 0;
};
#endif // DEQUE_INTERFACE_H