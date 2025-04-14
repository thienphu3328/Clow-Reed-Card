package com.example.clowreed

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.util.Random
import java.util.concurrent.TimeUnit

class RandomCard : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var button: Button

    private lateinit var sharedPreferences: SharedPreferences
    private var clickCount: Int = 0
    private var lastClickTime: Long = 0

    private val imageList = mutableListOf<ImageData>()

    private var isAnimating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.randomcard_layout)


        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView3)
        button = findViewById(R.id.button_draw)


        sharedPreferences = getSharedPreferences("ClickCount", Context.MODE_PRIVATE)
        clickCount = sharedPreferences.getInt("clickCount", 0)
        lastClickTime = sharedPreferences.getLong("lastClickTime", 0)

        // Thực hiện truy vấn cơ sở dữ liệu và lưu trữ danh sách hình ảnh và thông tin
        queryImageDataFromDatabase()

        button.setOnClickListener {
            handleButtonClick()
            // Lấy một hình ảnh ngẫu nhiên từ danh sách
            val randomImageData = getRandomImageData()

            // Sử dụng Glide để tải hình ảnh từ đường dẫn và hiển thị trong ImageView
            Glide.with(this).load(randomImageData.imageLink).into(imageView)

            if (!isAnimating) {
                flipCard()
            }

            updateButtonState()


            // Đặt thông tin tương ứng vào TextView
            textView.text = randomImageData.imageInfo
        }
    }

    private fun queryImageDataFromDatabase() {
        // Thực hiện truy vấn cơ sở dữ liệu và lưu trữ danh sách hình ảnh và thông tin
        // Thay thế đoạn mã này với logic truy vấn thực tế của bạn
        // Sau khi truy vấn thành công, cập nhật danh sách imageList
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/04.jpg",
                "Bạn cần phải xem lại sự sáng suốt và thông minh trong những quyết định của mình. Bạn nên thật thoải mái và thư giãn trước khi quyết định bất cứ điều gì hay phán xét bất kì một ai. Khi đã có một quyết định đúng đắn, cuộc sống của bạn sẽ dễ dàng và thoải mái hơn."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/05.jpg",
                "Hãy tự tin bày tỏ những suy nghĩ và tình cảm của mình, rồi bạn sẽ thấy thật nhẹ nhàng và thanh thản. Không những thế, bạn sẽ tích lũy được vô số kinh nghiệm trong lĩnh vực hội họa và âm nhạc. Nếu theo đuổi chúng, bạn sẽ luôn tìm ra cho mình những cảm hứng và ý tưởng tuyệt vời."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/06.jpg",
                "Bạn đang quá tải rồi đấy, sức lực bạn đang cạn kiệt. Vì thế, hãy điều chỉnh lại lịch làm việc đang quá dày đặc của bạn và hãy tự thưởng cho mình một kì nghỉ đúng nghĩa – không công việc, không lo lắng, một ngày mà bạn có thể làm những điều mà mình thấy thoải mái, dù cho bạn có bận rộn tới đâu."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/12.jpg",
                "Với ý chí và nghị lực mạnh mẽ, sẽ chẳng có khó khăn nào có thể làm bạn chùn bước, dù cho nó có thể đã đẩy bạn tới sự tuyệt vọng."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/21.jpg",
                "Tinh thần rạng rỡ của bạn rất dễ thu hút những người xung quanh. Mọi việc bạn làm sẽ đều thuận buồm xuôi gió, không những thế mà còn đạt được thành công mỹ mãn"
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/27.jpg",
                "Bạn chỉ còn một cơ hội để xem xét lại quyết định của mình mà thôi, hãy trân trọng nó. Đừng để sự tự ti làm mờ mắt bạn trong việc nhìn nhận và phân tích bất cứ vấn đề và tình hình nào."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/30.jpg",
                "Đừng lo lắng, trời sẽ ban cho bạn một “cơn mưa phước lành”. Bạn nên biết rằng bất cứ sự việc gì đến với ta cũng có nguyên do của nó. Không cách này thì cách khác, nó chắc chắn giúp bạn trưởng thành và mạnh mẽ hơn. Bi quan chẳng có lợi ích gì đâu, hãy thoải mái với nó vì cái gì tới, nó phải tới."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/38.jpg",
                "Đây là lúc mà bạn nên thay đổi quan điểm cá nhân và bắt đầu công việc một cách thật mới mẻ. Ngoài ra, đây cũng là thời gian thích hợp để bạn tìm tòi, học hỏi và tích lũy thêm kiến thức thiết thực cho mình. Bạn có thể có duyên gặp gỡ với những người tiền bối dày dặn kinh nghiệm trong lĩnh vực mà bạn đang tiến hành. Và họ sẽ không ngần ngại giúp đỡ bạn."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/40.jpg",
                "Những suy nghĩ áp lực đang đè nặng lên đôi vai bạn, và cảm giác sợ hãi cũng thế. Những suy nghĩ đó, những cảm giác đó trong bạn đã đi tới đỉnh điểm và  chúng gần như bùng nổ rồi đấy. Đừng lo lắng và bi quan, hãy tìm cách cải thiện nó, làm giảm bớt những suy nghĩ đó trong bạn. Cái gì tới cũng phải tới. Dù cho bạn có gặp những vấn đề thực sự nghiêm trọng thì cũng đừng quá hoảng loạn và tự đưa mình vào bế tắc. Hãy tự tin lên, hãy dùng sức mạnh và lí trí mạnh mẽ, kiên cường của bạn để giái quyết vấn đề đó. “Bởi vì đó chính là bạn, cái gì rồi cũng sẽ ổn thôi”- Sakura Kinomoto."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/44.jpg",
                "Thời gian sẽ thách thức bạn trong việc điều khiển cảm xúc của mình trong mọi việc. Bạn có thể dễ dàng tìm thấy rất nhiều ý tưởng nối tiếp nhau theo một trình tự nhất định. Ngoài ra, bạn dễ làm người khác ấn tượng và ngưỡng mộ bằng chính tính cách và suy nghĩ trưởng thành, độc đáo của bạn."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/48.jpg",
                "Sự thấu hiểu và cảm thông với mọi người chung quanh đem đến cho bạn sự tin tưởng và quý mến từ mọi người. Không những thế, sự nhạy bén và khả năng quan sát, đánh giá người khác hết sức tự nhiên và nhẹ nhàng của bạn rất có lợi cho công việc của bạn."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/49.jpg",
                "Này bạn của tôi, hãy nhớ rằng cuộc đời này không phải là một thảm hoa hồng trải sẵn mà nó đầy chông gai và thử thách. Và cuộc đời bạn cũng không ngoại lệ, cuộc đời bạn cũng có “nốt” trầm, “nốt” bổng, điều đó không quan trọng, vì bạn là bạn của tôi, và bạn chắc chắn sẽ gạt bỏ được những thất bại kia và tiến lên phía trước.\n" +
                        "\n" +
                        "Để hàn gắn lại một mối quan hệ không phải chuyện đơn giản, nhưng nó sẽ không khó nếu bạn tìm ra một giải pháp hoàn hảo, tưởng như rất xa nhưng lạ thay lại gần ngay trước mắt, trong chính con người bạn đã có được giải pháp ấy – sự rộng lượng và thấu hiểu.\n"
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/50.jpg",
                "Tới rồi, thời gian của sự thay đổi và những hành động đã điểm. Bạn sẽ có thể khám phá ra được rất nhiều thông tin bổ ích và quan trọng đối với bản thân mình hay vấn đề đang được đặt ra. Trong thời gian này, hãy nhớ rằng, sự thận trọng và kĩ lưỡng hơn trong việc quan sát tình hình xung quanh mình là không thừa. Và cuối cùng, một điều nữa: tại nơi này, vào lúc này, bạn cần phải đưa ra cho mình một quyết định."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/01.jpg",
                "Sự đam mê tìm tòi và học hỏi trong bạn đang ngày một tăng lên trong thời điểm này, thật đáng quý. Không những thế mà đây còn là cơ hội hiếm có để bạn có thể vượt qua vòng an toàn của mình và tiến tới một bậc thềm trình độ cao hơn, tốt đẹp hơn, chuyên môn hơn. Vì vậy, hãy tận dụng cơ hội này để hoàn thiện những kế hoạch đã đề ra và nâng cao cách làm việc của mình.\n" +
                        "\n" +
                        "Đồng thời, những tài lẻ của bạn sẽ đưa bạn tới một cuộc tình đầy bất ngờ, kéo bạn vào một giấc mơ hay đưa bạn tới con đường mà mình muốn chọn."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/08.jpg",
                "Bạn dễ dàng nhận được những thông điệp từ linh cảm và giấc mơ của mình – giấc mơ tiên tri, nhờ giác quan thứ sáu tuyệt vời của bạn. Đâu đó quanh bạn, sẽ có một số gợi ý giúp bạn tìm lại những thông điệp từ giấc mơ mà bạn đã vô tình bỏ lỡ. Điều này sẽ giúp bạn tìm lại cho mình một ít ngăn trống trong trái tim mình."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/17.jpg",
                "Những may mắn sẽ dần tìm đến với bạn, phá tan mọi sự âu lo và phiền muộn, cả những do dự trong lòng của bạn. Những việc bạn cần làm lúc này sẽ dần tìm tới bạn, thật tự nhiên và nhẹ nhàng. Tất cả những điều bạn cần làm lúc này chỉ là chuẩn bị cho mình thật kĩ lưỡng trước khi bắt tay vào thực hiện những công việc kia, để nó có một kết quả thật hoàn hảo."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/18.jpg",
                "Bạn thường nhìn nhận vấn đề, sự việc rất sơ sài. Bạn chỉ nhìn thấy bề nổi của sự vật, những thay đổi trước mắt mà không chịu tìm hiểu kĩ cái cốt lõi bên trong của nó. Nhưng hãy nhớ rằng, dù cho cuộc sống và môi trường làm việc có thay đổi như thế nào đi nữa thì đừng lo lắng. Bởi bạn vẫn có đủ khả năng để thích ứng với nó, vì đó chính là bản năng của bất cứ ai."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/22.jpg",
                "Những chướng ngại sẽ xuất hiện khiến bạn lo lắng, bất an và cảm thấy tuyệt vọng nhưng đừng lo, vì bạn sẽ luôn tìm được cách trấn an mình và vượt qua nó một cách dễ dàng hơn bao giờ hết. Nếu làm mọi việc từ tốn và kĩ lưỡng với một phong thái thật điềm tĩnh, ổn định thì những thành công sẽ lần lượt tìm đến bạn liên tục trong một thời gian dài đấy. Song song đó, việc tự tích lũy cho bản thân những điều cần thiết chắc chắn là không thừa."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/25.jpg",
                "Nếu bạn đã không thể làm cho công việc mình đi vào đúng quỹ đạo của nó hay sắp xếp thời gian cho nó một cách thực sự hợp lý, hãy tạm dừng. Ngồi xuống ghế với một tâm hồn thật thư giãn và thoải mái và hãy tìm câu trả lời cho những câu hỏi về bản thân:” Có phải mình đã quá ngoan cố?”, “Mình có phải đã quá hờ hững trước cảm xúc của người khác và chỉ nghĩ cho riêng mình thôi không?”, “ Sự hèn nhát đáng ghét ấy liệu đã đeo bám lấy mình chăng?”.\n" +
                        "Tìm được câu trả lời cho những câu hỏi trên, bạn sẽ tìm ra được cho mình “ Con Đường Nên Đi Lúc Này”."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/29.jpg",
                "Bạn là một người khá tò mò và thích khám phá. Cùng với sự kiên trì và nỗ lực, sự tò mò đó và tính nỗ lực sẽ hòa nhập lại làm một trong bạn. Chúng không những giúp bạn biến ước mơ của mình thành hiện thực mà hơn nữa, bạn còn giành được địa vị khá cao trong công việc mà bạn đang làm. Và sau này, cả tình yêu lẫn công việc của bạn sẽ ngày càng phát triển và tuyệt vời hơn bao giờ hết."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/36.jpg",
                "Dù cho bạn có cố tình thể hiện hay không thì ai cũng phải công nhận rằng bạn là một con người chính trực.\n" +
                        "\n" +
                        "Nếu như bạn có thể đảm nhiệm đúng vai trò mà mình nên làm trong cuộc sống và công việc thì những tin tức tốt lành sẽ đến mà bạn không ngờ."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/39.jpg",
                "Bạn chỉ có thể khiến người khác tôn trọng, lắng nghe quan điểm của mình khi và chỉ khi bạn thực sự học được cách tôn trọng người khác và quan điểm của họ mà thôi. Cuộc sống buồn chán ư? Hãy thử lập nên một kế hoạch thật sự mới mẻ và thực hiện chúng với bạn bè mình đi nào, hay bạn cũng có thể thử một chuyến du lịch dài ngày để xua tan cái buồn tẻ ấy.\n"
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/41.jpg",
                "Có một người thực sự quan trọng và đặc biệt với bạn, duy chỉ có một người có thể làm bạn dễ xiêu lòng chỉ bằng việc ở bên bạn. Tình cảm trong sáng ấy thật tuyệt vời và dễ thương, nó mang đến cho bạn hạnh phúc, niềm vui và một tâm trạng tuyệt vời mỗi ngày. Sẽ tốt hơn nữa nếu bạn làm việc cùng với một tâm trạng phơi phới như thế, công việc của bạn sẽ đạt được sự trơn tru và hoàn hảo.\n" +
                        "Và như thế, đời sống tình cảm của bạn cũng khá ổn định trong một khoảng thời gian."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/45.jpg",
                "“Đi một ngày đàng, học một sàng khôn.”\n" +
                        "Ngày qua ngày, chúng ta luôn học hỏi và tiếp thu được không ít thì nhiều những kinh nghiệm và biến chúng thành nền tảng đối nhân xử thế, nền tảng để sống tốt hơn mỗi ngày. Đó không phải là chuyện “ một sớm một chiều” mà là cả một quá trình tôi luyện và học hỏi dài đăng đẳng. Vì thế hãy tôn trọng hiện tại, quý trọng những gì mình đã tiếp thu được, để có thể có tâm trạng và sức khỏe tốt nhất."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/46.jpg",
                "Làm bất cứ việc cùng mọi người sẽ là lực chọn tốt nếu bạn mong sao công việc được suôn sẻ và có thật nhiều may mắn. Tại sao vậy? Vì khi làm việc theo nhóm, các bạn sẽ có cơ hội thể hiện thế mạnh của bản thân, cùng nhau chia sẻ kinh nghiệm, mỗi người đóng góp một chút, một chút, rồi công việc đang làm sẽ cực kì may mắn và thuận lợi. Bạn là một người quen biết rộng rãi và có một tâm hồn rộng rãi, điều này sẽ đem lại cho bạn rất nhiều may mắn và sự ủng hộ từ mọi người.\n"
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/47.jpg",
                "Có lẽ bạn đang khá mệt mỏi và khổ sở trong việc giao tiếp với những người hàng xóm của mình phải không? Vấn đề nằm ở chỗ có lẽ bạn chưa biết điều chỉnh cảm xúc của mình thật thích hợp khi trao đổi cùng họ mà thôi. Hãy từ từ thay đổi cách xã giao với những người láng giềng của mình, bắt đầu bằng các câu xã giao đơn giản như “ chào buổi sáng” hoặc chỉ đơn giản là “ xin chào” chắc không phải là một việc khó với bạn đâu nhỉ?\n"
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/03.jpg",
                "Bạn không kiểm soát được sự tập trung của mình. Bạn thường sinh lòng ghen ghét, đố kị với người khác. Không chỉ vậy, bạn còn nhìn nhận mọi thứ chung quanh một cách hết sức tiêu cực. Những áp lực này, những lo âu và rối bời trong tâm trí đã làm cho tâm trí bạn nặng trĩu. Bạn đã lựa chọn cho mình cách thư giãn bằng việc mua sắm thật nhiều, thật nhiều mà không hay biết rằng việc làm xa xỉ đó đang lấy đi của bạn rất nhiều tiền bạc và sức lực đấy.\n"
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/07.jpg",
                "Đỉnh cao của khát vọng đang ở phía trước và chờ bạn chinh phục. Có phải bạn đang có một cuộc chiến tranh nội tâm? Đừng lo lắng, hãy cứ để nó tiếp tục, hãy đễ cho nó tự nhiên vì nó sẽ tạo nên cho bạn nguồn sức mạnh để vươn tới khát vọng kia đấy. Những chông gai và thử thách là điều mà không ai có thể tránh được, có thể nó sẽ làm bạn tổn thương dù nhiều hay ít nhưng hãy đừng lùi bước. Hãy chạy thật nhanh, hãy lao tới mặc cho chúng có bất cứ hành động gì, để đạt được mục đích của mình. Bạn nghĩ nó là một sự cố chấp ư? Không, đó là đam mê và khát vọng."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/10.jpg",
                "Thứ nhất, hãy tự hỏi lại bản thân mình liệu có phải bạn đã lên kế hoạch cho tiền bạc của mình hơi nhiều không? Bạn có phải đã vung khá nhiều tiền vào những ham muốn cá nhân không?\n" +
                        "\n" +
                        "Thứ hai, dù cho bạn chưa hoàn thành được kế hoạch mà mình đã đề ra, đừng thất vọng. Hãy tiếp tục nỗ lực và chờ đợi cơ hội lần sau."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/11.jpg",
                "Bạn sẽ được hậu tạ từ rất nhiều người nhận bạn là ân nhân, khi bạn đã là kim chỉ Nam giúp đỡ họ trong lúc họ khốn khó nhất. Trong lúc này, sức mạnh và sự minh mẫn của bạn sẽ được cải thiện hơn bao giờ hết, và những xui xẻo cũng sẽ dần bỏ đi.\n"
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/13.jpg",
                "Nếu bạn thực sự có ý chí nỗ lực, chính bạn sẽ cứu mình khỏi những áp lực đang đè nặng lên đôi vai. Ngược lại, nếu bạn chỉ luôn luẩn quẩn trong “ vòng an toàn” của mình, đó chính là sự bắt đầu cho những rắc rối mãi không thể gỡ nút được.\n" +
                        "\n" +
                        "Nhớ rằng, việc chú ý tới những mối quan hệ chung quanh mình, kể cả chuyện tình cảm của mình là không thừa."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/15.jpg",
                "Những mối quan hệ chung quanh bạn đang ngày càng phát triển cũng như vận may của bạn vậy. Mọi việc bạn làm sẽ ít gặp những khó khăn, thực sự trơn tru và dễ dàng.\n" +
                        "\n" +
                        "Vì vậy, đừng ngần ngại tham gia, chấp nhận những thử thách hay những bài kiểm tra chất lượng."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/16.jpg",
                "Nếu một năm có bốn mùa Xuân, Hạ, Thu, Đông, thì cuộc sống này cũng vậy, cũng có đủ bốn mùa. Nếu mùa xuân là lúc trăm hoa đâm chồi nảy lộc, sức sống tràn đầy khắp mọi nơi thì mùa đông lại là lúc chúng thu mình lại, bước vào thời kì nghỉ ngơi và dưỡng sức, sức sống lúc này cũng phai dần đi. Cũng như thế, vào lúc này, mùa đông của bạn đã đến, đây chính là lúc bạn cần dành cho mình sự nghỉ ngơi, nạp lại cho bản thân đầy năng lượng để có thể bung xõa trong mùa xuân sắp tới. Đừng quá rụt rè hay lo sợ mà vội vã làm hỏng chuyện. Hãy cứ từ từ kiểm soát năng lượng bản thân mình và hoàn thành công việc một cách hoàn hảo nhất.\n"
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/19.jpg",
                "Bằng trái tim thấu hiểu, thông cảm và đầy tình yêu thương của mình, bạn dễ dàng truyền sức mạnh và động lực tuyệt vời của mình cho người khác, thật đáng ngưỡng mộ. Đồng thời, sự thân thiện và gần gũi của bạn với mọi người cũng khiến bạn được nhiều người yêu quý, đó chính là nguồn sức mạnh vô giá của bạn đấy.\n" +
                        "\n" +
                        "Bạn thật là bận rộn, cả công lẫn tư, bạn khó khi nào thoát khỏi nó. Nhưng đừng lo lắng, dù cho thời gian làm việc có kín tới đâu thì bạn vẫn còn thời gian nghỉ ngơi dưỡng sức mà."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/28.jpg",
                "Bạn có thể sẽ không gặp may mắn một thời gian chính vì những lời phê bình người khác với thái độ cứng nhắc và quá bồng bột, hoặc cũng có thể là vì bạn đã làm những hành động khá kì quặc.\n" +
                        "\n" +
                        "Nhớ rằng hãy coi lại bản thân, đừng để bạn tự biến mình thành một kẻ chuyên đi bép xép và thổi phồng mọi chuyện."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/31.jpg",
                "Bạn có phải đã tiếc nuối vì một chuyện mà mình chưa bao giờ làm được? Bạn có phải đã từng trốn tránh những vấn đề quá nan giải không? Dù như vậy nhưng trong thân tâm bạn luôn muốn có một lần nữa có cơ hội thực hiện chúng, hoàn thành chúng, để không phải nuối tiếc về sau, và giờ cơ hội đấy đã đến rồi.\n" +
                        "\n" +
                        "“ The Return” xuất hiện cũng có nghĩa là bạn đã có đủ năng lực hoàn thành những việc mình đã hoặc đang tránh né, bạn có đủ sức mạnh và ý chí để tự khẳng định mình trước mọi người. Đừng do dự nữa, một quyết định nhanh chóng và sáng suốt rất có lợi cho bạn vào lúc này."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/35.jpg",
                "Lòng tham lam của bạn sẽ dẫn bạn đến thất bại không tưởng, hãy cẩn thận. Hãy chắc chắn những mục đích mà bạn đặt ra là không quá dư thừa và thực sự cần thiết cho bản thân mình.\n" +
                        "\n" +
                        "Sự tự cao và khinh người rất dễ hình thành nếu bạn không biết điều khiển cảm xúc và thái độ của mình. Vì vậy, hãy nghiêm khắc hơn với bản thân trong mọi việc, hãy biết tự trách bản thân mỗi khi làm sai hỏng bất cứ việc lớn nhỏ gì, có thể bạn mới có thể trở thành một con người thành đạt và khiêm tốn được."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/37.jpg",
                "Bạn đang có một cuộc sống thật an nhàn và bình thường. Không có quá nhiều những rắc rối hay những cuộc xung đột nghiêm trọng. Vì vậy, tại sao bạn không thử biến mỗi ngày của bạn là mỗi ngày tràn ngập hạnh phúc, sự chân thành và trung thực?\n" +
                        "\n" +
                        "Nếu như có bất kì khó khăn nào trên con đường thử thách của mình thì cũng đừng dừng lại, cứ hướng thẳng mà tiến tới, bọn chúng ắt sẽ lánh xa bạn và bạn sẽ có thêm nhiều may mắn đấy."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/43.jpg",
                "Chìa khóa của thành công chính là sự thay đổi. Nói thay đổi ở đây không phải thay đổi về kiểu cách, cách ăn mặc mà về quan điểm, về suy nghĩ, về nhận định và về lối sống tinh thần. Một khi bản thân bạn đã sự thay đổi như thế đồng nghĩa với việc cuộc sống của bạn sẽ tiến dần, phát triển theo chiều hướng đi lên. Đồng thời, vận may cũng sẽ tìm tới bạn bằng cách này, hoặc cách khác, dĩ nhiên nó sẽ đến.\n" +
                        "\n" +
                        "Nhưng quan trọng hơn hết vẫn là chính kiến của bạn. Đừng a dua, đừng ba phải, bạn phải có chính kiến riêng của mình chứ? Chỉ một từ thôi, “ không” hay “ có”!"
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/00.jpg",
                "Năng lượng của bạn hiện nay có thể coi là hoàn hảo, đủ để bạn có thể bước đi trên con đường đi tới mục tiêu của mình rồi đấy.\n" +
                        "\n" +
                        "Mục đích ấy sẽ đạt được càng sớm nếu bạn biết tận dụng tất cả sức lực, hành động, lí trí của bạn vào nó. Rồi bạn sẽ đạt được mục tiêu của mình sớm thôi."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/02.jpg",
                "Mọi lo lắng và phiền muộn trong bạn sẽ dần tan biến như làn khói, trả lại cho bạn sự thanh thản và thoải mái trong tâm hồn.\n" +
                        "\n" +
                        "Đừng cố gắng thay đổi, phá cách hay cố làm bất cứ thứ gì có ảnh hưởng tới công việc mà bạn đang làm, kể cả việc cố gắng thể hiện bản thân nữa. Nếu không, có thể bạn sẽ lâm vào bế tắc."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/09.jpg",
                "Thời gian này bạn nên tập trung xây dựng cho mình một nền tảng thật chắc chắn và kiên cố cho công việc hay dự định của mình. Nhưng cũng đừng quá chăm chút cho nó mà quên mất việc cần làm để có thể phát triển công việc đó một cách toàn diện. Dù cho những việc bạn đang làm chưa thể hiện cái hiệu quả như bạn mong muốn thì cũng đừng quá thất vọng nhé, vì chúng không ít thì nhiều cũng sẽ giúp bạn có thêm nhiều cơ hội phát triển hơn nữa cùng những điều vô cùng bổ ích."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/14.jpg",
                "Những nỗ lực và cố gắng của bạn sẽ sớm đơm hoa kết trái, bạn sẽ được hoàn thành được tâm nguyện của mình. Lòng tin vào bản thân và lối suy nghĩ tích cực sẽ dẫn bạn đến với con đường mà bạn đang tìm kiếm đấy, đừng nản chí.\n"
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/20.jpg",
                "Hãy thật bình tĩnh và công bằng khi đưa ra bất cứ quyết định cho bất cứ vấn đề gì. Và hãy nhớ, việc chọn một quyết định khiêm tốn không có gì là xấu cả.\n" +
                        "\n" +
                        "Đồng thời, những rắc rối khiến bạn đứng ngồi không yên, khiến bạn mất cân bằng trong cuộc sống thời gian qua sẽ dần biến mất. Trạng thái của bạn sẽ dần hồi phục và tinh thần bạn sẽ tốt nhất."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/23.jpg",
                "Sự thật có thể sẽ được đem ra ngoài ánh sáng, nhưng cũng có thể mãi mãi khép lại và không bao giờ mở ra nữa. Hãy học cách lắng nghe, thấu hiểu con tim mình, khi đó, bạn có thể tìm hiểu bí mật của người khác dễ dàng hơn. Nhưng, tốt nhất, bạn nên để mọi thứ thuận theo tự nhiên, đừng tác động vào nó.\n"
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/24.jpg",
                "Bạn có thể dễ dàng kết nối với người khác cũng như những vật chung quanh bạn bằng sự cảm thông, thấu hiểu và tình cảm nhẹ nhàng nơi bạn đối với họ. Bên cạnh đó, bạn còn nhận được những tấm lòng hết lòng giúp đỡ mình qua sự thân thiện và chân thành với người khác.\n"
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/26.jpg",
                "Thông qua các hành động và cách ứng xử của người khác, bạn sẽ có cơ hội nhìn nhận lại bản thân một lần nữa, nhìn ra được những ưu điểm và cả những khuyết điểm của bản thân mình.\n"
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/32.jpg",
                "Cảm xúc con người là thứ muôn màu muôn vẻ, khó ai có thể hiểu và nắm bắt được nó một cách hoàn toàn. Nó rất phức tạp, luôn biến đổi mọi giây mọi phút, và dĩ nhiên, chúng tồn tại ở vô số hình dạng khác nhau. Vì vậy, để đánh giá, nhìn nhận, hay phán đoán cảm xúc của bất kì ai, chúng ta không chỉ dừng lại ở việc nhìn nhận một khía cạnh được, bởi nó là chưa đủ.\n" +
                        "\n" +
                        "Bạn đang có mâu thuẫn với bạn bè, người yêu mình? Hãy làm lành với họ, từng bước, từng bước. Nếu quá vội vàng, có thể bạn sẽ làm cho tình hình xấu đi đấy."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/33.jpg",
                "Những điều vô hình trên không gì khác chính là những rắc rối, chúng ôm chặt bạn như hình với bóng, luôn không tách rời. Có bao giờ bạn tự hỏi tại sao chúng lại xuất hiện không? Vì chúng muốn nhắc nhở bạn rằng con đường mà bạn đang đi sẽ không bao giờ có thể tới được đích đến cả, hãy suy nghĩ, cân nhắc, quyết định lại.\n"
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/34.jpg",
                "Ngay cả khi bạn có lâm vào khủng hoảng và rắc rối to lớn, đừng lo sợ. Vì chung quanh bạn vẫn đang còn những người thân, bạn bè của bạn vẫn luôn siết chặt vòng tay, tạo thành màng chắn bảo vệ cho bạn khỏi những thế lực bên ngoài. Màng chắn ấy chính được hình thành bằng tình thương vô bờ bến của những người chung quanh với bạn, cũng như sự hòa thuận giữa các bạn vậy. Đó là thứ chắc chắn nhất trên đời và khó ai có thể làm nó lung lay được. Bởi vậy, hãy biết quý trọng nó bây giờ và mãi mãi.\n"
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/42.jpg",
                "“The Sword” xuất hiện chính là đang nhắc nhở bạn về lòng trung thực và cả quyền lực nữa.\n" +
                        "\n" +
                        "Chính lúc này đây, thời gian đã chín muồi, đây là lúc thật tuyệt vời để bạn thực hiện những dự định, mong ước của mình. Đừng lo lắng, hãy bước đi, trên con đường mình mong ước.\n" +
                        "\n" +
                        "Nhân quả tuần hoàn vẫn luôn hiện diện. Hãy luôn nói sự thật, phần thưởng xứng đáng sẽ sớm về tay bạn. Hãy gieo xuống mầm sống của sự thật, bạn sẽ gặt được cả vụ mùa bội thu."
            )
        )
        imageList.add(
            ImageData(
                "https://tarot.vn/wp-content/uploads/2015/08/51.jpg",
                "Sự trưởng thành và phát triển một cách vững chắc.\n" +
                        "\n" +
                        "Trong các mối quan hệ cá nhân, bạn dễ nhận được sự gắn bó và đồng cảm từ mọi người. Bên cạnh đó, tài năng và thực lực của bạn sẽ có cơ hội tỏa sáng trong thời gian tới, và tất nhiên là bạn sẽ thu về được rất nhiều sự tán dương và ngưỡng mộ."
            )
        )


    }

    private fun getRandomImageData(): ImageData {
        // Lấy một hình ảnh ngẫu nhiên từ danh sách imageList
        val randomIndex = (0 until imageList.size).random()
        return imageList[randomIndex]
    }

    private fun handleButtonClick() {


        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - lastClickTime

        if (clickCount < 1 || elapsedTime >= TimeUnit.HOURS.toMillis(1)) {
            val randomIndex = Random().nextInt(52)
            val imageUrl = "https://tarot.vn/wp-content/uploads/2015/08/$randomIndex.jpg"

            loadImage(imageUrl)

            clickCount++
            lastClickTime = currentTime

            sharedPreferences.edit()
                .putInt("clickCount", clickCount)
                .putLong("lastClickTime", lastClickTime)
                .apply()

            updateButtonState()
        } else {
            Toast.makeText(this, "Here is the message for you", Toast.LENGTH_SHORT).show()
        }
    }
    // hàm giới hạn số lần bấm là 1
    private fun updateButtonState() {
        button.isEnabled = clickCount < 1
    }

    private fun loadImage(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .into(imageView)
    }


    private var isBackVisible = true // Mặt trước đang hiển thị ban đầu

    private fun flipCard() {
        val imageViewCard = findViewById<ImageView>(R.id.imageView)

        if (isBackVisible) {
            // Xoay lá bài từ mặt trước ra mặt sau
            val animator = ObjectAnimator.ofFloat(imageViewCard, "rotationY", 180f, 0f)
            animator.duration = 500
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    isAnimating = true
                }

                override fun onAnimationEnd(animation: Animator) {
                    isBackVisible = false
                    isAnimating = false

                }

                override fun onAnimationCancel(animation: Animator) {
                    isAnimating = false
                }
            })
            animator.start()
        }else{
                // Xoay lá bài từ mặt trước ra mặt sau
                val animator = ObjectAnimator.ofFloat(imageViewCard, "rotationY", 0f, 180f)
                animator.duration = 500
                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        isAnimating = true
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        isBackVisible = true
                        isAnimating = false
                        val frontImageResId = R.drawable.clowcardexample
                        imageViewCard.setImageResource(frontImageResId)
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        isAnimating = false
                    }
                })
                animator.start()
            }
        }
    }



data class ImageData(val imageLink: String, val imageInfo: String)