
import styles from './index.css';
import Link from 'umi/link';
import HelloWorld from './HelloWorld';
export default function() {
    return (
        <div className={styles.normal}>
            <h1>Page index</h1>
            <Link to={"HelloWorld"}>go to hello world</Link>
            <br/>
            <Link to={"List"}>go to list</Link>
            <br/>
            <Link to={"MyTabs"}>go to tabs</Link>
            <HelloWorld name = "zhangsan">传智播客</HelloWorld>
        </div>
    );
}
