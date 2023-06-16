import java.applet.*;
import java.awt.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.image.TextureLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewBilliardsTest extends Applet {

	// 基本のパラメータ
	// 的玉の数
	private int ballNumber;
	// 玉の半径
	private float ballR;

	// 基本のパラメータのゲッター
	public int getBallNumber() {
		return this.ballNumber;
	}

	public float getBallR() {
		return this.ballR;
	}

	// ボールの半径のもとに成り立つ応用のパラメータ
	// バットの長さ
	private float batHigh;
	// シャフトの長さ
	private float shaftHigh;
	// ティップの長さ
	private float tipHigh;
	// キューの半径
	private float cueR;

	// 台の縦幅
	private float standWidthX;
	// 台の厚さ
	private float standWidthY;
	// 台の横幅
	private float standWidthZ;

	// 玉の下げ幅
	private float ballDownWidth;
	// 玉の左寄せ幅
	private float ballMoveWidth;

	// 手玉と的玉の距離
	private float distance0To1;

	// 応用のパラメータのゲッター
	public float getBatHigh() {
		return this.batHigh;
	}

	public float getShaftHigh() {
		return this.shaftHigh;
	}

	public float getTipHigh() {
		return this.tipHigh;
	}

	public float getCueR() {
		return this.cueR;
	}

	public float getStandWidthX() {
		return this.standWidthX;
	}

	public float getStandWidthY() {
		return this.standWidthY;
	}

	public float getStandWidthZ() {
		return this.standWidthZ;
	}

	public float getBallDownWidth() {
		return this.ballDownWidth;
	}

	public float getBallMoveWidth() {
		return this.ballMoveWidth;
	}

	public float getDistance0To1() {
		return this.distance0To1;
	}

	// 玉を並べる場所に関するパラメータ
	// 玉の中心の各座標
	private float basicX, basicY, basicZ;
	// 的玉を正三角形型に並べるとき、各玉の中心の座標を格納するリスト
	List<BallPosition> basicBall = new ArrayList<BallPosition>();
	// 最終的に玉を並べたとき、各玉の中心座標を格納するリスト
	List<BallPosition> ball = new ArrayList<BallPosition>();

	public float getBasicX() {
		return this.basicX;
	}

	public float getBasicY() {
		return this.basicY;
	}

	public float getBasicZ() {
		return this.basicZ;
	}

	// 各的玉の回転を格納するリスト
	List<BallRotation> ballRotation = new ArrayList<BallRotation>();

	// このプログラムの基本となるパラメータの設定
	private void setBasicParameters(int ballNumber, float ballR) {
		// 的玉の数
		this.ballNumber = ballNumber;
		// 玉の半径
		this.ballR = ballR;
	}

	// ボールの半径のもとに成り立つパラメータの設定
	private void setApplicationParameters() {
		// バットの長さ
		this.batHigh = getBallR() * (float) 20;
		// シャフトの長さ
		this.shaftHigh = getBallR() * (float) 20;
		// ティップの長さ
		this.tipHigh = getBallR() * (float) 0.6;
		// キューの半径
		this.cueR = getBallR() * (float) 0.4;

		// 台の縦幅
		this.standWidthX = getBallR() * (float) 40;
		// 台の厚さ
		this.standWidthY = getBallR() / (float) 2.0;
		// 台の横幅
		this.standWidthZ = getBallR() * (float) 22;

		// 玉の下げ幅
		this.ballDownWidth = getBallR() * (float) 12;
		// 玉の左寄せ幅
		this.ballMoveWidth = getBallR() * (float) 20;

		// 手玉と的玉の距離
		this.distance0To1 = getBallR() * 50;
	}

	// 玉を並べる場所に関する設定
	private void setBasicBall() {

		// 的玉を正三角形型に並べるとき、各玉の中心の座標を実際に格納する(方法2:玉の数無制限)
		// 各行未作成のボールの数を格納する
		int count = 0;
		// 行数を格納する
		int countRow = 0;
		// ある数列の値
		int countColumn = 1;

		for (int i = 0; i <= getBallNumber(); i++) {
			if (i == 0) {
				this.basicX = getDistance0To1();
				this.basicY = 0.0f;
				this.basicZ = 0.0f;
				countRow += 1;
				count = countRow;
			} else {
				this.basicX = -(getBallR() + getBallR() * (float) Math.sqrt(3) * (float) (countRow - 1));
				this.basicY = 0.0f;
				this.basicZ = -getBallR() * (float) countRow + getBallR()
						+ getBallR() * (float) 2.0 * (float) (i - countColumn);

				count -= 1;
				if (count == 0) {
					countRow += 1;
					count = countRow;
					countColumn += (countRow - 1);
				}
			}

			// キューを入れるためにボールを全体的に左に寄せる
			this.basicX -= getBallMoveWidth();
			// キューを入れるためにボールを全体的に下に下げる
			this.basicY -= getBallDownWidth();

			basicBall.add(new BallPosition(i, getBasicX(), getBasicY(), getBasicZ()));
		}
	}

	// エイトボールの並べ方設定
	private void setEightBallPosition() {
		if (getBallNumber() == 15) {

			// 既出の位置を格納するリスト
			List<Integer> alreadyMentionedBall = new ArrayList<Integer>();
			alreadyMentionedBall.add(basicBall.get(0).getId());
			alreadyMentionedBall.add(basicBall.get(5).getId());
			alreadyMentionedBall.add(basicBall.get(basicBall.size() - 1).getId());
			alreadyMentionedBall.add(basicBall.get(basicBall.size() - 1 - 4).getId());

			// ランダムに一つのローボールを選ぶ
			Random lowBallRandom = new Random();
			int lowBall = lowBallRandom.nextInt(7);

			// ランダムに一つのハイボールを選ぶ
			Random highBallRandom = new Random();
			int highBall = highBallRandom.nextInt(7) + 9;

			for (int i = 0; i <= getBallNumber(); i++) {
				// 手玉は最初に設定した位置に置く
				if (i == 0) {
					ball.add(basicBall.get(0));
				}
				// 8のボールを三行目二列目に置く
				else if (i == 8) {
					ball.add(basicBall.get(5));
				}
				// 一番右下をローボールに指定する
				else if (i == lowBall) {
					ball.add(basicBall.get(15));
				} else if (i == highBall) {
					ball.add(basicBall.get(11));
				}
				// その他のボール
				else {
					while (true) {
						// ランダムに一つの場所
						Random otherBallRandom = new Random();
						int otherBall = otherBallRandom.nextInt(15) + 1;

						// loopOtherBall2の中身を何回繰り返したかを数える
						int countLoop = 0;

						for (int j = 0; j < alreadyMentionedBall.size(); j++) {
							// もし選んだ場所が既出だった場合、もう一度選び直す
							if (otherBall == alreadyMentionedBall.get(j)) {
								break;
							} else {
								countLoop++;
							}
						}

						if (countLoop == alreadyMentionedBall.size()) {
							ball.add(basicBall.get(otherBall));
							alreadyMentionedBall.add(basicBall.get(otherBall).getId());
							break;
						}
					}
				}
			}
		} else {
			for (int i = 0; i <= getBallNumber(); i++) {
				ball.add(basicBall.get(i));
			}
			System.out.println("的玉が15個でないとき、エイトボールの並べ方は実行できません。");
		}
	}

	// ボールの回転を設定するメソッド
	private void setBallRotation() {
		for (int i = 0; i <= getBallNumber(); i++) {
			// ランダムに回転を得る
			float rotationX = (float) (Math.PI / Math.random() * 2.0);
			float rotationY = (float) (Math.PI / Math.random() * 2.0);
			float rotationZ = (float) (Math.PI / Math.random() * 2.0);

			ballRotation.add(new BallRotation(ball.get(i).getId(), rotationX, rotationY, rotationZ));
		}
	}

	// キューを台の表面に移動させるメソッド
	private Transform3D cueTransform3DYAxis() {
		Transform3D cueTransform3DYAxis = new Transform3D();
		cueTransform3DYAxis.setTranslation(new Vector3f(0.0f, -(getBallDownWidth() + getBallR() - getCueR()), 0.0f));
		return cueTransform3DYAxis;
	}

	// キューを台の中心寄りに移動させるメソッド
	private Transform3D cueTransform3DXAxis() {
		Transform3D cueTransform3DXAxis = new Transform3D();
		cueTransform3DXAxis.setTranslation(new Vector3f(-(getBallMoveWidth() / (float) 2.0), 0.0f, 0.0f));
		return cueTransform3DXAxis;
	}

	// キューを斜めに回転させるメソッド
	private Transform3D cueTransform3DRotationYAxis() {
		Transform3D cueTransform3DRotationYAxis = new Transform3D();
		cueTransform3DRotationYAxis.setRotation(new AxisAngle4f(0.0f, 1.0f, 0.0f, (float) Math.PI / 8.0f));
		return cueTransform3DRotationYAxis;
	}

	// キューを移動させるメソッド
	private Transform3D cueTransform3DMove() {
		Transform3D cueTransform3DMove = new Transform3D();
		cueTransform3DMove.mul(cueTransform3DRotationYAxis());
		cueTransform3DMove.mul(cueTransform3DXAxis());
		cueTransform3DMove.mul(cueTransform3DYAxis());
		return cueTransform3DMove;
	}

	// シャフトのテクスチャを設定するメソッド
	private TextureLoader shaftTextureLoader() {
		TextureLoader shaftTextureLoader = new TextureLoader("./texture/wood.jpg", this);
		return shaftTextureLoader;
	}

	// 尻ゴム(buttRubber)を作成するメソッド
	private void createButtRubber(TransformGroup Billiards) {

		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance buttRubberAppearance = new BilliardsAppearance();
		// 色
		applyBaseColorButtRubber(buttRubberAppearance.getMaterial());
		// 球の生成
		Sphere buttRubberSphere = new Sphere(getCueR(), Primitive.GENERATE_NORMALS, 100,
				buttRubberAppearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup buttRubberBilliards = new TransformGroup();
		// 生成物の変換
		Transform3D buttRubberTransform3DRotationXAxis = new Transform3D();
		buttRubberTransform3DRotationXAxis.setRotation(new AxisAngle4f(0.0f, 0.0f, 1.0f, (float) Math.PI / 2.0f));
		Transform3D buttRubberTransform3DYAxis = new Transform3D();
		buttRubberTransform3DYAxis
				.setTranslation(new Vector3f(getTipHigh() / (float) 2.0 + getShaftHigh() + getBatHigh(), 0.0f, 0.0f));
		// 全変換の合成
		Transform3D buttRubberTransform3D = new Transform3D();
		buttRubberTransform3D.mul(cueTransform3DMove());
		buttRubberTransform3D.mul(buttRubberTransform3DYAxis);
		buttRubberTransform3D.mul(buttRubberTransform3DRotationXAxis);
		buttRubberBilliards.setTransform(buttRubberTransform3D);
		// 生成物の最終を接続
		buttRubberBilliards.addChild(buttRubberSphere);
		Billiards.addChild(buttRubberBilliards);
	}

	// バット(bat)を作成するメソッド
	private void createBat(TransformGroup Billiards) {

		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance batAppearance = new BilliardsAppearance();
		// 色
		applyBaseColorBat(batAppearance.getMaterial());
		// 円柱の生成
		Cylinder batCylinder = new Cylinder(getCueR(), getBatHigh(), Primitive.GENERATE_NORMALS, 100, 100,
				batAppearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup batBilliards = new TransformGroup();
		// 生成物の変換
		Transform3D batTransform3DRotationXAxis = new Transform3D();
		batTransform3DRotationXAxis.setRotation(new AxisAngle4f(0.0f, 0.0f, 1.0f, (float) Math.PI / 2.0f));
		Transform3D batTransform3DYAxis = new Transform3D();
		batTransform3DYAxis.setTranslation(
				new Vector3f(getTipHigh() / (float) 2.0 + getShaftHigh() + getBatHigh() / (float) 2.0, 0.0f, 0.0f));
		// 全変換の合成
		Transform3D batTransform3D = new Transform3D();
		batTransform3D.mul(cueTransform3DMove());
		batTransform3D.mul(batTransform3DYAxis);
		batTransform3D.mul(batTransform3DRotationXAxis);
		batBilliards.setTransform(batTransform3D);
		// 生成物の最終を接続
		batBilliards.addChild(batCylinder);
		Billiards.addChild(batBilliards);
	}

	// シャフト(shaft)を作成するメソッド
	private void createShaft(TransformGroup Billiards) {

		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance shaftAppearance = new BilliardsAppearance();
		// 色
		applyBaseColorShaft(shaftAppearance.getMaterial());
		// テクスチャの設定
		shaftAppearance.getAppearance().setTexture(shaftTextureLoader().getTexture());
		// 円柱の生成
		Cylinder shaftCylinder = new Cylinder(getCueR(), getShaftHigh(), Primitive.GENERATE_NORMALS, 100, 100,
				shaftAppearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup shaftBilliards = new TransformGroup();
		// 生成物の変換
		Transform3D shaftTransform3DRotationXAxis = new Transform3D();
		shaftTransform3DRotationXAxis.setRotation(new AxisAngle4f(0.0f, 0.0f, 1.0f, (float) Math.PI / 2.0f));
		Transform3D shaftTransform3DYAxis = new Transform3D();
		shaftTransform3DYAxis
				.setTranslation(new Vector3f(getTipHigh() / (float) 2.0 + getShaftHigh() / (float) 2.0, 0.0f, 0.0f));
		// 全変換の合成
		Transform3D shaftTransform3D = new Transform3D();
		shaftTransform3D.mul(cueTransform3DMove());
		shaftTransform3D.mul(shaftTransform3DYAxis);
		shaftTransform3D.mul(shaftTransform3DRotationXAxis);
		shaftBilliards.setTransform(shaftTransform3D);
		// 生成物の最終を接続
		shaftBilliards.addChild(shaftCylinder);
		Billiards.addChild(shaftBilliards);
	}

	// ティップ(Tip) 先端の石を作成するメソッド
	private void createTip(TransformGroup Billiards) {

		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance tipAppearance = new BilliardsAppearance();
		// 色
		applyBaseColorTip(tipAppearance.getMaterial());
		// 円柱の生成
		Cylinder tipCylinder = new Cylinder(getCueR(), getTipHigh(), Primitive.GENERATE_NORMALS, 100, 100,
				tipAppearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup tipBilliards = new TransformGroup();
		// 生成物の変換
		Transform3D tipTransform3DRotationXAxis = new Transform3D();
		tipTransform3DRotationXAxis.setRotation(new AxisAngle4f(0.0f, 0.0f, 1.0f, (float) Math.PI / 2.0f));
		// 全変換の合成
		Transform3D tipTransform3D = new Transform3D();
		tipTransform3D.mul(cueTransform3DMove());
		tipTransform3D.mul(tipTransform3DRotationXAxis);
		tipBilliards.setTransform(tipTransform3D);
		// 生成物の最終を接続
		tipBilliards.addChild(tipCylinder);
		Billiards.addChild(tipBilliards);
	}

	// キューを作成するメソッド
	private void createCue(TransformGroup Billiards) {
		// 尻ゴム(buttRubber)を作成
		createButtRubber(Billiards);
		// バット(bat)を作成
		createBat(Billiards);
		// シャフト(shaft)を作成
		createShaft(Billiards);
		// ティップ(Tip) 先端の石を作成
		createTip(Billiards);
	}

	// 台(stand)を作成するメソッド
	private void createStand(TransformGroup Billiards) {

		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance standAppearance = new BilliardsAppearance();
		// 色
		applyBaseColorStand(standAppearance.getMaterial());
		// 直方体の生成
		Box standBox = new Box(getStandWidthX(), getStandWidthY(), getStandWidthZ(), Primitive.GENERATE_NORMALS,
				standAppearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup standBilliards = new TransformGroup();
		// 生成物の変換
		Transform3D standTransform3DXAxis = new Transform3D();
		standTransform3DXAxis
				.setTranslation(new Vector3f(0.0f, -(getBallDownWidth() + getBallR() + getStandWidthY()), 0.0f));
		// 全変換の合成
		Transform3D standTransform3D = new Transform3D();
		standTransform3D.mul(standTransform3DXAxis);
		standBilliards.setTransform(standTransform3D);
		// 生成物の最終を接続
		standBilliards.addChild(standBox);
		Billiards.addChild(standBilliards);
	}

	// ボールを設定したボール位置に移動させるメソッド
	private Transform3D ballTransform3DMove(int thisBall) {
		Transform3D ballTransform3DMove = new Transform3D();
		ballTransform3DMove.setTranslation(new Vector3f(ball.get(thisBall).getPositionX(),
				ball.get(thisBall).getPositionY(), ball.get(thisBall).getPositionZ()));
		return ballTransform3DMove;
	}

	// ボールを回転させるメソッド
	private Transform3D ballTransform3DRotation(int thisBall) {
		Transform3D ballTransform3DRotationXAxis = new Transform3D();
		Transform3D ballTransform3DRotationYAxis = new Transform3D();
		Transform3D ballTransform3DRotationZAxis = new Transform3D();
		ballTransform3DRotationXAxis
				.setRotation(new AxisAngle4f(1.0f, 0.0f, 0.0f, ballRotation.get(thisBall).getRotationX()));
		ballTransform3DRotationYAxis
				.setRotation(new AxisAngle4f(0.0f, 1.0f, 0.0f, ballRotation.get(thisBall).getRotationY()));
		ballTransform3DRotationZAxis
				.setRotation(new AxisAngle4f(0.0f, 0.0f, 1.0f, ballRotation.get(thisBall).getRotationZ()));
		Transform3D ballTransform3DRotation = new Transform3D();
		ballTransform3DRotation.mul(ballTransform3DRotationZAxis);
		ballTransform3DRotation.mul(ballTransform3DRotationYAxis);
		ballTransform3DRotation.mul(ballTransform3DRotationXAxis);
		return ballTransform3DRotation;
	}

	// ボールのテクスチャを設定するメソッド
	private TextureLoader ballTextureLoader(int thisBall) {
		TextureLoader ballTextureLoader = new TextureLoader("./texture/originalBilliardsBall" + thisBall + ".jpg", this);
		return ballTextureLoader;
	}

	// 0玉（ball）を作成するメソッド
	private void createBall0(TransformGroup Billiards) {

		// ボールの数字を格納するパラメータ
		int thisBall = 0;
		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance ball0Appearance = new BilliardsAppearance();
		// 色
		applyBaseColorBall(ball0Appearance.getMaterial());
		// テクスチャの設定
		ball0Appearance.getAppearance().setTexture(ballTextureLoader(thisBall).getTexture());
		// 球の生成
		Sphere ball0Sphere = new Sphere(getBallR(), Primitive.GENERATE_TEXTURE_COORDS, 100,
				ball0Appearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup ball0Billiards = new TransformGroup();
		// 全変換の合成
		Transform3D ball0Transform3D = new Transform3D();
		ball0Transform3D.mul(ballTransform3DMove(thisBall));
		ball0Transform3D.mul(ballTransform3DRotation(thisBall));
		ball0Billiards.setTransform(ball0Transform3D);
		// 生成物の最終を接続
		ball0Billiards.addChild(ball0Sphere);
		Billiards.addChild(ball0Billiards);
	}

	// 1玉（ball）を作成するメソッド
	private void createBall1(TransformGroup Billiards) {

		// ボールの数字を格納するパラメータ
		int thisBall = 1;
		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance ball1Appearance = new BilliardsAppearance();
		// 色
		applyBaseColorBall(ball1Appearance.getMaterial());
		// テクスチャの設定
		ball1Appearance.getAppearance().setTexture(ballTextureLoader(thisBall).getTexture());
		// 球の生成
		Sphere ball1Sphere = new Sphere(getBallR(), Primitive.GENERATE_TEXTURE_COORDS, 100,
				ball1Appearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup ball1Billiards = new TransformGroup();
		// 全変換の合成
		Transform3D ball1Transform3D = new Transform3D();
		ball1Transform3D.mul(ballTransform3DMove(thisBall));
		ball1Transform3D.mul(ballTransform3DRotation(thisBall));
		ball1Billiards.setTransform(ball1Transform3D);
		// 生成物の最終を接続
		ball1Billiards.addChild(ball1Sphere);
		Billiards.addChild(ball1Billiards);
	}

	// 2玉（ball）を作成するメソッド
	private void createBall2(TransformGroup Billiards) {

		// ボールの数字を格納するパラメータ
		int thisBall = 2;
		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance ball2Appearance = new BilliardsAppearance();
		// 色
		applyBaseColorBall(ball2Appearance.getMaterial());
		// テクスチャの設定
		ball2Appearance.getAppearance().setTexture(ballTextureLoader(thisBall).getTexture());
		// 球の生成
		Sphere ball2Sphere = new Sphere(getBallR(), Primitive.GENERATE_TEXTURE_COORDS, 100,
				ball2Appearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup ball2Billiards = new TransformGroup();
		// 全変換の合成
		Transform3D ball2Transform3D = new Transform3D();
		ball2Transform3D.mul(ballTransform3DMove(thisBall));
		ball2Transform3D.mul(ballTransform3DRotation(thisBall));
		ball2Billiards.setTransform(ball2Transform3D);
		// 生成物の最終を接続
		ball2Billiards.addChild(ball2Sphere);
		Billiards.addChild(ball2Billiards);
	}

	// 3玉（ball）を作成するメソッド
	private void createBall3(TransformGroup Billiards) {

		// ボールの数字を格納するパラメータ
		int thisBall = 3;
		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance ball3Appearance = new BilliardsAppearance();
		// 色
		applyBaseColorBall(ball3Appearance.getMaterial());
		// テクスチャの設定
		ball3Appearance.getAppearance().setTexture(ballTextureLoader(thisBall).getTexture());
		// 球の生成
		Sphere ball3Sphere = new Sphere(getBallR(), Primitive.GENERATE_TEXTURE_COORDS, 100,
				ball3Appearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup ball3Billiards = new TransformGroup();
		// 全変換の合成
		Transform3D ball3Transform3D = new Transform3D();
		ball3Transform3D.mul(ballTransform3DMove(thisBall));
		ball3Transform3D.mul(ballTransform3DRotation(thisBall));
		ball3Billiards.setTransform(ball3Transform3D);
		// 生成物の最終を接続
		ball3Billiards.addChild(ball3Sphere);
		Billiards.addChild(ball3Billiards);
	}

	// 4玉（ball）を作成するメソッド
	private void createBall4(TransformGroup Billiards) {

		// ボールの数字を格納するパラメータ
		int thisBall = 4;
		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance ball4Appearance = new BilliardsAppearance();
		// 色
		applyBaseColorBall(ball4Appearance.getMaterial());
		// テクスチャの設定
		ball4Appearance.getAppearance().setTexture(ballTextureLoader(thisBall).getTexture());
		// 球の生成
		Sphere ball4Sphere = new Sphere(getBallR(), Primitive.GENERATE_TEXTURE_COORDS, 100,
				ball4Appearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup ball4Billiards = new TransformGroup();
		// 全変換の合成
		Transform3D ball4Transform3D = new Transform3D();
		ball4Transform3D.mul(ballTransform3DMove(thisBall));
		ball4Transform3D.mul(ballTransform3DRotation(thisBall));
		ball4Billiards.setTransform(ball4Transform3D);
		// 生成物の最終を接続
		ball4Billiards.addChild(ball4Sphere);
		Billiards.addChild(ball4Billiards);
	}

	// 5玉（ball）を作成するメソッド
	private void createBall5(TransformGroup Billiards) {

		// ボールの数字を格納するパラメータ
		int thisBall = 5;
		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance ball5Appearance = new BilliardsAppearance();
		// 色
		applyBaseColorBall(ball5Appearance.getMaterial());
		// テクスチャの設定
		ball5Appearance.getAppearance().setTexture(ballTextureLoader(thisBall).getTexture());
		// 球の生成
		Sphere ball5Sphere = new Sphere(getBallR(), Primitive.GENERATE_TEXTURE_COORDS, 100,
				ball5Appearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup ball5Billiards = new TransformGroup();
		// 全変換の合成
		Transform3D ball5Transform3D = new Transform3D();
		ball5Transform3D.mul(ballTransform3DMove(thisBall));
		ball5Transform3D.mul(ballTransform3DRotation(thisBall));
		ball5Billiards.setTransform(ball5Transform3D);
		// 生成物の最終を接続
		ball5Billiards.addChild(ball5Sphere);
		Billiards.addChild(ball5Billiards);
	}

	// 6玉（ball）を作成するメソッド
	private void createBall6(TransformGroup Billiards) {

		// ボールの数字を格納するパラメータ
		int thisBall = 6;
		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance ball6Appearance = new BilliardsAppearance();
		// 色
		applyBaseColorBall(ball6Appearance.getMaterial());
		// テクスチャの設定
		ball6Appearance.getAppearance().setTexture(ballTextureLoader(thisBall).getTexture());
		// 球の生成
		Sphere ball6Sphere = new Sphere(getBallR(), Primitive.GENERATE_TEXTURE_COORDS, 100,
				ball6Appearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup ball6Billiards = new TransformGroup();
		// 全変換の合成
		Transform3D ball6Transform3D = new Transform3D();
		ball6Transform3D.mul(ballTransform3DMove(thisBall));
		ball6Transform3D.mul(ballTransform3DRotation(thisBall));
		ball6Billiards.setTransform(ball6Transform3D);
		// 生成物の最終を接続
		ball6Billiards.addChild(ball6Sphere);
		Billiards.addChild(ball6Billiards);
	}

	// 7玉（ball）を作成するメソッド
	private void createBall7(TransformGroup Billiards) {

		// ボールの数字を格納するパラメータ
		int thisBall = 7;
		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance ball7Appearance = new BilliardsAppearance();
		// 色
		applyBaseColorBall(ball7Appearance.getMaterial());
		// テクスチャの設定
		ball7Appearance.getAppearance().setTexture(ballTextureLoader(thisBall).getTexture());
		// 球の生成
		Sphere ball7Sphere = new Sphere(getBallR(), Primitive.GENERATE_TEXTURE_COORDS, 100,
				ball7Appearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup ball7Billiards = new TransformGroup();
		// 全変換の合成
		Transform3D ball7Transform3D = new Transform3D();
		ball7Transform3D.mul(ballTransform3DMove(thisBall));
		ball7Transform3D.mul(ballTransform3DRotation(thisBall));
		ball7Billiards.setTransform(ball7Transform3D);
		// 生成物の最終を接続
		ball7Billiards.addChild(ball7Sphere);
		Billiards.addChild(ball7Billiards);
	}

	// 8玉（ball）を作成するメソッド
	private void createBall8(TransformGroup Billiards) {

		// ボールの数字を格納するパラメータ
		int thisBall = 8;
		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance ball8Appearance = new BilliardsAppearance();
		// 色
		applyBaseColorBall(ball8Appearance.getMaterial());
		// テクスチャの設定
		ball8Appearance.getAppearance().setTexture(ballTextureLoader(thisBall).getTexture());
		// 球の生成
		Sphere ball8Sphere = new Sphere(getBallR(), Primitive.GENERATE_TEXTURE_COORDS, 100,
				ball8Appearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup ball8Billiards = new TransformGroup();
		// 全変換の合成
		Transform3D ball8Transform3D = new Transform3D();
		ball8Transform3D.mul(ballTransform3DMove(thisBall));
		ball8Transform3D.mul(ballTransform3DRotation(thisBall));
		ball8Billiards.setTransform(ball8Transform3D);
		// 生成物の最終を接続
		ball8Billiards.addChild(ball8Sphere);
		Billiards.addChild(ball8Billiards);
	}

	// 9玉（ball）を作成するメソッド
	private void createBall9(TransformGroup Billiards) {

		// ボールの数字を格納するパラメータ
		int thisBall = 9;
		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance ball9Appearance = new BilliardsAppearance();
		// 色
		applyBaseColorBall(ball9Appearance.getMaterial());
		// テクスチャの設定
		ball9Appearance.getAppearance().setTexture(ballTextureLoader(thisBall).getTexture());
		// 球の生成
		Sphere ball9Sphere = new Sphere(getBallR(), Primitive.GENERATE_TEXTURE_COORDS, 100,
				ball9Appearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup ball9Billiards = new TransformGroup();
		// 全変換の合成
		Transform3D ball9Transform3D = new Transform3D();
		ball9Transform3D.mul(ballTransform3DMove(thisBall));
		ball9Transform3D.mul(ballTransform3DRotation(thisBall));
		ball9Billiards.setTransform(ball9Transform3D);
		// 生成物の最終を接続
		ball9Billiards.addChild(ball9Sphere);
		Billiards.addChild(ball9Billiards);
	}

	// 10玉（ball）を作成するメソッド
	private void createBall10(TransformGroup Billiards) {

		// ボールの数字を格納するパラメータ
		int thisBall = 10;
		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance ball10Appearance = new BilliardsAppearance();
		// 色
		applyBaseColorBall(ball10Appearance.getMaterial());
		// テクスチャの設定
		ball10Appearance.getAppearance().setTexture(ballTextureLoader(thisBall).getTexture());
		// 球の生成
		Sphere ball10Sphere = new Sphere(getBallR(), Primitive.GENERATE_TEXTURE_COORDS, 100,
				ball10Appearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup ball10Billiards = new TransformGroup();
		// 全変換の合成
		Transform3D ball10Transform3D = new Transform3D();
		ball10Transform3D.mul(ballTransform3DMove(thisBall));
		ball10Transform3D.mul(ballTransform3DRotation(thisBall));
		ball10Billiards.setTransform(ball10Transform3D);
		// 生成物の最終を接続
		ball10Billiards.addChild(ball10Sphere);
		Billiards.addChild(ball10Billiards);
	}

	// 11玉（ball）を作成するメソッド
	private void createBall11(TransformGroup Billiards) {

		// ボールの数字を格納するパラメータ
		int thisBall = 11;
		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance ball11Appearance = new BilliardsAppearance();
		// 色
		applyBaseColorBall(ball11Appearance.getMaterial());
		// テクスチャの設定
		ball11Appearance.getAppearance().setTexture(ballTextureLoader(thisBall).getTexture());
		// 球の生成
		Sphere ball11Sphere = new Sphere(getBallR(), Primitive.GENERATE_TEXTURE_COORDS, 100,
				ball11Appearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup ball11Billiards = new TransformGroup();
		// 全変換の合成
		Transform3D ball11Transform3D = new Transform3D();
		ball11Transform3D.mul(ballTransform3DMove(thisBall));
		ball11Transform3D.mul(ballTransform3DRotation(thisBall));
		ball11Billiards.setTransform(ball11Transform3D);
		// 生成物の最終を接続
		ball11Billiards.addChild(ball11Sphere);
		Billiards.addChild(ball11Billiards);
	}

	// 12玉（ball）を作成するメソッド
	private void createBall12(TransformGroup Billiards) {

		// ボールの数字を格納するパラメータ
		int thisBall = 12;
		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance ball12Appearance = new BilliardsAppearance();
		// 色
		applyBaseColorBall(ball12Appearance.getMaterial());
		// テクスチャの設定
		ball12Appearance.getAppearance().setTexture(ballTextureLoader(thisBall).getTexture());
		// 球の生成
		Sphere ball12Sphere = new Sphere(getBallR(), Primitive.GENERATE_TEXTURE_COORDS, 100,
				ball12Appearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup ball12Billiards = new TransformGroup();
		// 全変換の合成
		Transform3D ball12Transform3D = new Transform3D();
		ball12Transform3D.mul(ballTransform3DMove(thisBall));
		ball12Transform3D.mul(ballTransform3DRotation(thisBall));
		ball12Billiards.setTransform(ball12Transform3D);
		// 生成物の最終を接続
		ball12Billiards.addChild(ball12Sphere);
		Billiards.addChild(ball12Billiards);
	}

	// 13玉（ball）を作成するメソッド
	private void createBall13(TransformGroup Billiards) {

		// ボールの数字を格納するパラメータ
		int thisBall = 13;
		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance ball13Appearance = new BilliardsAppearance();
		// 色
		applyBaseColorBall(ball13Appearance.getMaterial());
		// テクスチャの設定
		ball13Appearance.getAppearance().setTexture(ballTextureLoader(thisBall).getTexture());
		// 球の生成
		Sphere ball13Sphere = new Sphere(getBallR(), Primitive.GENERATE_TEXTURE_COORDS, 100,
				ball13Appearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup ball13Billiards = new TransformGroup();
		// 全変換の合成
		Transform3D ball13Transform3D = new Transform3D();
		ball13Transform3D.mul(ballTransform3DMove(thisBall));
		ball13Transform3D.mul(ballTransform3DRotation(thisBall));
		ball13Billiards.setTransform(ball13Transform3D);
		// 生成物の最終を接続
		ball13Billiards.addChild(ball13Sphere);
		Billiards.addChild(ball13Billiards);
	}

	// 14玉（ball）を作成するメソッド
	private void createBall14(TransformGroup Billiards) {

		// ボールの数字を格納するパラメータ
		int thisBall = 14;
		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance ball14Appearance = new BilliardsAppearance();
		// 色
		applyBaseColorBall(ball14Appearance.getMaterial());
		// テクスチャの設定
		ball14Appearance.getAppearance().setTexture(ballTextureLoader(thisBall).getTexture());
		// 球の生成
		Sphere ball14Sphere = new Sphere(getBallR(), Primitive.GENERATE_TEXTURE_COORDS, 100,
				ball14Appearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup ball14Billiards = new TransformGroup();
		// 全変換の合成
		Transform3D ball14Transform3D = new Transform3D();
		ball14Transform3D.mul(ballTransform3DMove(thisBall));
		ball14Transform3D.mul(ballTransform3DRotation(thisBall));
		ball14Billiards.setTransform(ball14Transform3D);
		// 生成物の最終を接続
		ball14Billiards.addChild(ball14Sphere);
		Billiards.addChild(ball14Billiards);
	}

	// 15玉（ball）を作成するメソッド
	private void createBall15(TransformGroup Billiards) {

		// ボールの数字を格納するパラメータ
		int thisBall = 15;
		// Appearance(透明度とマッピング)Material(色彩設定と光の反射)
		BilliardsAppearance ball15Appearance = new BilliardsAppearance();
		// 色
		applyBaseColorBall(ball15Appearance.getMaterial());
		// テクスチャの設定
		ball15Appearance.getAppearance().setTexture(ballTextureLoader(thisBall).getTexture());
		// 球の生成
		Sphere ball15Sphere = new Sphere(getBallR(), Primitive.GENERATE_TEXTURE_COORDS, 100,
				ball15Appearance.getAppearance());
		// 子プリミティブの生成
		TransformGroup ball15Billiards = new TransformGroup();
		// 全変換の合成
		Transform3D ball15Transform3D = new Transform3D();
		ball15Transform3D.mul(ballTransform3DMove(thisBall));
		ball15Transform3D.mul(ballTransform3DRotation(thisBall));
		ball15Billiards.setTransform(ball15Transform3D);
		// 生成物の最終を接続
		ball15Billiards.addChild(ball15Sphere);
		Billiards.addChild(ball15Billiards);
	}

	// ボールを作成するメソッド
	private void createBall(TransformGroup Billiards) {
		// 0玉（ball）を作成
		createBall0(Billiards);
		// 1玉（ball）を作成
		createBall1(Billiards);
		// 2玉（ball）を作成
		createBall2(Billiards);
		// 3玉（ball）を作成
		createBall3(Billiards);
		// 4玉（ball）を作成
		createBall4(Billiards);
		// 5玉（ball）を作成
		createBall5(Billiards);
		// 6玉（ball）を作成
		createBall6(Billiards);
		// 7玉（ball）を作成
		createBall7(Billiards);
		// 8玉（ball）を作成
		createBall8(Billiards);
		// 9玉（ball）を作成
		createBall9(Billiards);
		// 10玉（ball）を作成
		createBall10(Billiards);
		// 11玉（ball）を作成
		createBall11(Billiards);
		// 12玉（ball）を作成
		createBall12(Billiards);
		// 13玉（ball）を作成
		createBall13(Billiards);
		// 14玉（ball）を作成
		createBall14(Billiards);
		// 15玉（ball）を作成
		createBall15(Billiards);
	}

	// 尻ゴムの色を与えるメソッド
	private void applyBaseColorButtRubber(Material material) {
		material.setDiffuseColor(new Color3f(0 / 255f, 0 / 255f, 0 / 255f));
	}

	// バットの色を与えるメソッド
	private void applyBaseColorBat(Material material) {
		material.setDiffuseColor(new Color3f(0 / 255f, 9 / 255f, 45 / 255f));
	}

	// シャフトの色を与えるメソッド
	private void applyBaseColorShaft(Material material) {
		material.setDiffuseColor(new Color3f(255 / 255f, 0 / 255f, 0 / 255f));
	}

	// ティップの色を与えるメソッド
	private void applyBaseColorTip(Material material) {
		material.setDiffuseColor(new Color3f(68 / 255f, 68 / 255f, 68 / 255f));
	}

	// 台の色を与えるメソッド
	private void applyBaseColorStand(Material material) {
		material.setDiffuseColor(new Color3f(0 / 255f, 109 / 255f, 18 / 255f));
	}

	// ボールの色を与えるメソッド
	private void applyBaseColorBall(Material material) {
		material.setDiffuseColor(new Color3f(0.0f, 0.0f, 1.0f));
	}

	// プリミティブを作成するメソッド
	private void createPrimitives(TransformGroup Billiards) {

		// 基本パラメータを設定
		setBasicParameters(15, 0.025f);
		// 応用パラメータを設定
		setApplicationParameters();
		// ボールを置く位置を設定
		setBasicBall();
		// エイトボールの並べ方を実行
		setEightBallPosition();
		// ボールの回転を設定
		setBallRotation();

		// キューを作成
		createCue(Billiards);
		// 台(stand)を作成
		createStand(Billiards);
		// ボールを作成
		createBall(Billiards);
	}

	// ウィンドウの設定
	public NewBilliardsTest() {
		// 描画領域であるCanvas3Dを作成し、Appletウィンドウの内部に配置する
		Canvas3D canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		this.setLayout(new BorderLayout());
		this.add(canvas3D, BorderLayout.CENTER);

		// SimpleUniverseを作成し、標準的な投影変換を行うように設定する
		SimpleUniverse simpleUniverse = new SimpleUniverse(canvas3D);
		simpleUniverse.getViewingPlatform().setNominalViewingTransform();

		// BranchGroupを作成する。このメソッドの最後でSimpleUniverseに接続する
		// 以下のプログラムではbgの下にシーングラフのノードを接続していく。
		BranchGroup bg = new BranchGroup();

		// 平行光源を作成し、bgに接続する
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setInfluencingBounds(new BoundingSphere());
		bg.addChild(directionalLight);

		// シーン全体の座標変換を行うTransformGroupを作成する。
		TransformGroup tg = new TransformGroup();

		// マウスによる操作を行うために、MouseRotateクラスのインスタンスを作成し、
		// tgに接続する
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		MouseRotate mouseRotate = new MouseRotate(tg);
		mouseRotate.setSchedulingBounds(new BoundingSphere());
		tg.addChild(mouseRotate);
		MouseTranslate mouseTranslate = new MouseTranslate(tg);
		mouseTranslate.setSchedulingBounds(new BoundingSphere());
		tg.addChild(mouseTranslate);
		MouseZoom mouseZoom = new MouseZoom(tg);
		mouseZoom.setSchedulingBounds(new BoundingSphere());
		tg.addChild(mouseZoom);

		// createPrimtivesメソッドを呼び出しtgに接続させる
		createPrimitives(tg);

		// tgへの接続がすべて終わったので、bgに接続する
		bg.addChild(tg);

		// SimpleUniverseにBranchGroupを接続する
		simpleUniverse.addBranchGraph(bg);
	}

	// ウィンドウサイズの設定、java3Dの表示
	public static void main(String[] args) {
		new MainFrame(new NewBilliardsTest(), 640, 480);
	}
}