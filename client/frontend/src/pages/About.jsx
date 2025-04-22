import '../styles/About.css';
import image from '../assets/About-Us Pic.png'
export default function About() {
    return (
        <div className="about-container">
            <h1>About Us</h1>
            <p>
                The Medical Research Institute is a leading center for medical innovation, bringing together scientists, doctors, and researchers from different specializations.
            </p>
            <br />
            <p>
                We offer free laboratory services to support medical studies, early diagnosis, and patient care. Our mission is to bridge the gap between research and real-world medical solutions, ensuring accessible and high-quality healthcare for all.
            </p>
            <div className='about'>
                <div className='about-text'>
                    <h1>نبذة عنا </h1>
                    <p>
                        معهد البحوث الطبية هو مركز رائد في الابتكار الطبي، يجمع بين العلماء والأطباء والباحثين من مختلف التخصصات
                    </p>
                    <br />
                    <p>
                        نقدم خدمات مختبرية مجانية لدعم الدراسات الطبية والتشخيص المبكر ورعاية المرضى. تتمثل مهمتنا في سد الفجوة بين الأبحاث والحلول الطبية الواقعية، لضمان توفير رعاية صحية عالية الجودة ومتاحة للجميع
                    </p>
                </div>
                <div className='about-img'>
                    <img src={image} alt='about img' />
                </div>
            </div>
        </div>
    );
} 