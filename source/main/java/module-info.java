import com.acromere.weave.Weave;

module com.acromere.weave {

	// Compile-time only
	requires static lombok;

	// Both compile-time and run-time
	requires com.acromere.zevra;
	requires com.acromere.zerra;
	requires com.acromere.zenna;
	requires javafx.controls;

	exports com.acromere.weave;
	exports com.acromere.weave.icon;

	provides com.acromere.product.Product with Weave;
}
