package wigets;

import my.monopoly.R;
import resource.ResourceBundle;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class CellView extends FrameLayout {
	private boolean checked = false;
	private int desiredX, desiredY;

	// reserved for future usage
	private String name, setname;
	private TextView viewName;

	private ResourceBundle bundle = null;
	private ImageView setIcon, cellIcon, ownerIcon, starIcon, checkIcon,
			mortgageIcon;

	private int setid, owner, stras, cell, objCellId;
	boolean mortgage;

	public CellView(Context context) {
		super(context);
		init();
	}

	public CellView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CellView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}



	public int getCell() {
		return cell;
	}

	/**
	 * @return the desiredX
	 */
	public int getDesiredX() {
		return desiredX;
	}

	/**
	 * @return the desiredY
	 */
	public int getDesiredY() {
		return desiredY;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return the objCellId
	 */
	public int getObjCellId() {
		return objCellId;
	}

	public int getOwner() {
		return owner;
	}

	public int getSetid() {
		return setid;
	}

	public String getSetname() {
		return setname;
	}

	public int getStras() {
		return stras;
	}

	private void init() {

		setIcon = new ImageView(getContext());
		cellIcon = new ImageView(getContext());
		ownerIcon = new ImageView(getContext());
		starIcon = new ImageView(getContext());
		checkIcon = new ImageView(getContext());
		mortgageIcon = new ImageView(getContext());
		checkIcon.setImageResource(R.drawable.check);
		checkIcon.setVisibility(INVISIBLE);
		viewName = new TextView(getContext());
		viewName.setTextColor(Color.BLACK);
		try {
			bundle= ResourceBundle.getSharedInstance();
		} catch (RuntimeException e) {
			// forces to run as extrenal controll
			bundle = ResourceBundle.getInstance(getContext());
		}
	}

	public boolean isChecked() {
		return checked;
	}

	/**
	 * @return the mortgage
	 */
	public boolean isMortgage() {
		return mortgage;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (oldw == 0) {
			FrameLayout.LayoutParams params;
			// Add and init views
			int vw = (int) (w * 0.75), vh = (int) (h * 0.75);
			int vx = (int) (w * 0.15), vy = (int) (h * 0.1);
			params = new FrameLayout.LayoutParams(vw, vh);
			params.setMargins(vx, vy, 0, 0);
			cellIcon.setLayoutParams(params);
			setIcon.setLayoutParams(params);
			cellIcon.setScaleType(ScaleType.FIT_CENTER);
			setIcon.setScaleType(ScaleType.FIT_CENTER);
			addView(cellIcon);
			addView(setIcon);
			params = new FrameLayout.LayoutParams(vw, vh);
			params.setMargins(vx, vy, 0, 0);
			viewName.setLayoutParams(params);
			addView(viewName);
			vw = (int) (w * 0.25);
			vh = (int) (h * 0.8);
			vx = (int) (w * 0.75);
			vy = (int) (h * 0.1);
			params = new FrameLayout.LayoutParams(vw, vh);
			params.setMargins(vx, vy, 0, 0);
			starIcon.setLayoutParams(params);
			starIcon.setScaleType(ImageView.ScaleType.FIT_START);
			// starIcon.setRotation(90);
			addView(starIcon);

			mortgageIcon.setLayoutParams(params);
			mortgageIcon.setScaleType(ImageView.ScaleType.FIT_START);
			addView(mortgageIcon);

			vw = (int) (w * 0.30);
			vh = (int) (h * 0.50);
			vx = (int) (w * 0.4);
			vy = (int) (h * 0.1);

			params = new FrameLayout.LayoutParams(vw, vh);
			params.setMargins(vx, vy, 0, 0);
			// ownerIcon.setAlpha(0.5f);
			addView(ownerIcon, params);

			vw = (int) (w * 0.30);
			vh = (int) (h * 0.30);
			vx = (int) (w * 0.70);
			vy = (int) (h * 0.70);

			params = new FrameLayout.LayoutParams(vw, vh);
			params.setMargins(vx, vy, 0, 0);
			// ownerIcon.setAlpha(0.5f);
			addView(checkIcon, params);

		}

	}

	public void setCell(int cell) {
		this.cell = cell;
		cellIcon.setImageDrawable(bundle.cells[cell]);
	}

	public void setChecked(boolean checked) {
		if (owner >= 0) {
			this.checked = checked;

			if (checked) {
				checkIcon.setVisibility(VISIBLE);
			} else {
				checkIcon.setVisibility(INVISIBLE);
			}
			invalidate();
		}
	}

	/**
	 * @param desiredX
	 *            the desiredX to set
	 */
	public void setDesiredX(int desiredX) {
		this.desiredX = desiredX;
	}

	/**
	 * @param desiredY
	 *            the desiredY to set
	 */
	public void setDesiredY(int desiredY) {
		this.desiredY = desiredY;
	}

	/**
	 * @param mortgage
	 *            the mortgage to set
	 */
	public void setMortgage(boolean mortgage) {
		this.mortgage = mortgage;

		this.mortgageIcon
				.setImageDrawable(bundle.mortgage[!mortgage ? 0 : 1]);
	}

	public void setName(String name) {
		this.name = name;
		if (name == null)
			this.name = "N/A";
		viewName.setText(this.name);
	}

	public void setObjCellId(int objCellId) {
		this.objCellId = objCellId;
	}

	public void setOwner(int owner) {
		this.owner = owner;
		ownerIcon.setImageDrawable(bundle.playersFlags[owner + 1]);
	}

	public void setSetid(int setid) {
		this.setid = setid;
		// setBackgroundDrawable(bundle.sets[setid]);

		setIcon.setImageDrawable(bundle.sets[setid]);

	}

	public void setSetname(String setname) {
		this.setname = setname;
	}

	public void setStras(int stras) {
		if (stras > 0) {
			setChecked(false);
		}
		this.stras = stras;
		this.starIcon.setImageDrawable(bundle.starsv[stras]);
	}

}
