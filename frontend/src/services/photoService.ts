import axios from 'axios';

const api = axios.create({ baseURL: 'http://localhost:8181/api'});

export interface CameraSettings {
    cameraModel?: string;
    lens?: string;
    aperture?: string;
    shutterSpeed?: string;
    iso?: number;
    focalLength?: string;
}

export interface Photo {
    id: string;
    title: string;
    description?: string;
    fileName: string;
    filePath: string;
    fileType?: string;
    fileSize?: number;
    captureDate: string;
    uploadDate?: string;
    lastEditedDate?: string;
    cameraSettings?: CameraSettings;
    location?: string;
    tags?: string[];
    editingSoftware?: string;
    isEdited: boolean;
    thumbnailPath?: string;
    isPublic: boolean;
}

export async function fetchPhotos(): Promise<Photo[]>{
    const {data} = await api.get<Photo[]>('/photos');
    return data;
}