import '../styles/Home.css';
import image from '../assets/Animated_Doctors.png'
export default function Home() {
    return (
        <div className="home-container">
<div className='home'>
<div className='home-text'>
                <h1>Welcome to MRI</h1>
                <h1>معهد البحوث الطبيه</h1>
                <br/>
                <h2>Advancing Medical Research for a Healthier Future</h2>
                <h2>تعزيز الأبحاث الطبية من أجل مستقبل أكثر صحة</h2>
            </div>
            <div className='home-img'>
                <img src={image} alt='home img' />
            </div>
</div>
        </div>
    );
}



