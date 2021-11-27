#ifndef ZIPCONTAINER_H
#define ZIPCONTAINER_H


#include <utility>

template <typename V1, typename V2>
class ZipContainer {
private:
	V1& cRef1;
	V2& cRef2;

public:
	class ZipIterator {
	private:
		typename V1::iterator iter1;
		typename V2::iterator iter2;

	public:
		ZipIterator(typename V1::iterator iter1, typename V2::iterator iter2) : iter1(iter1), iter2(iter2) {}

		std::pair<typename V1::value_type, typename V2::value_type> operator*() {
			return make_pair(*iter1, *iter2);
		}

		bool operator!=(const ZipIterator& other) {
			return (iter1 != other.iter1) && (iter2 != other.iter2);
		}

		ZipIterator operator++() {
			++iter1;
			++iter2;

			return *this;
		}

		const ZipIterator operator++(int) {
			ZipIterator result = *this;

			++(*this);

			return result;
		}
	};

	ZipContainer(V1& cRef1, V2& cRef2) : cRef1(cRef1), cRef2(cRef2) {}

	ZipIterator begin() {
		return ZipIterator(cRef1.begin(), cRef2.begin());
	}

	ZipIterator end() {
		return ZipIterator(cRef1.end(), cRef2.end());
	}
};


#endif //ZIPCONTAINER_H
